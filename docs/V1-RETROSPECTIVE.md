# V1 회고 — 무엇을 만들려 했고, 왜 멈췄는가

이 문서는 `legacy/mkdp-v1.0.0/`에 남은 코드를 근거로 작성했다. 원래 설계
문서는 Jira/Confluence(`mkdp.atlassian.net`)에 있었지만 2년이 지난 지금
접근 권한이 없어, 코드에서 의도를 역추적한 결과다.

## 무엇을 만들려 했나

패키지 구조와 컨트롤러/DTO 이름에서 의도가 드러난다.

- 회원가입/로그인 (`LoginController`, `MemberService`, `InMemorySessionManager`)
- DART 공시·기업정보 조회 (`BusinessController`)
- **포트폴리오 백테스트** — 여러 기업을 조합해 수익률/MDD를 계산하는 기능
  (`BacktestRequestVO`, `BacktestResultVO`, `AssetAllocationVO`, `PortfolioValueVO`)
- 프론트엔드에 `backtest-portfolio.vue`, `portfolio.vue` 컴포넌트 존재

한국판 portfolio-visualizer.com을 만들려 했던 것으로 보인다: 기업을 검색해
포트폴리오를 구성하고, 과거 데이터로 백테스트하는 흐름.

## 왜 멈췄나

### 1. 설계 단계의 근본적인 공백 — 가격 데이터가 없다

`BusinessController.runBacktest()`의 실제 구현부는 아래처럼 전부 주석 처리돼
있다.

```java
// 2. 각 기업의 주가 데이터 조회
// Map<String, List<StockPriceVO>> stockData = getStockPriceData(
//     request.getCompanyCodes(),
//     request.getStartDate(),
//     request.getEndDate()
// );

// 3. 백테스트 수행
// BacktestResultVO backtestResult = performBacktest(...)
```

`getStockPriceData`, `performBacktest` 같은 메서드는 프로젝트 어디에도
존재하지 않는다. `StockPriceVO`도 참조만 되고 클래스 자체가 없다.

이건 미완성이 아니라 **애초에 막힌 지점**이다. DART 오픈API는 공시·재무·기업정보만
제공하고 **일별 주가는 제공하지 않는다.** 백테스트에는 반드시 일별 가격 시계열이
필요한데, 그 데이터를 어디서 가져올지에 대한 답이 없었던 것으로 보인다. 이 프로젝트가
DART만으로는 절대 완성될 수 없는 기능을 핵심 기능으로 설계했다는 뜻이다.

**v2의 대응**: 백테스트/포트폴리오 기능을 MVP 범위에서 제외했다. DART가 실제로
제공하는 공시·재무 조회에만 집중한다.

### 2. 기업 코드 동기화 — 세 가지 버그가 겹쳐 있었다

`updateCorpCodes()`가 DART `corpCode.xml`(ZIP)을 받아 DB에 적재하는 기능인데,
세 단계 모두 개별적으로 깨져 있었다.

**(a) InputStream을 문자열화 — 다운로드한 바이트를 전부 버림**

```java
byte[] zipFile = String.valueOf(apiResponse.getEntity().getContent()).getBytes();
```

`getContent()`는 `InputStream`을 반환한다. `String.valueOf(inputStream)`은
스트림 내용을 읽는 게 아니라 `Object.toString()` 기본 구현(예:
`org.apache.http.conn.EofSensorInputStream@1a2b3c4d` 같은 메모리 주소 문자열)을
호출한다. 결국 `zipFile`은 실제 ZIP 바이트가 아니라 **객체 메모리 주소를 문자로
표현한 것**이었다. 이 시점에서 이후 모든 처리는 유효하지 않은 데이터로 진행된다.

**(b) 하드코딩된 태그 이름 — 설령 ZIP이 제대로 들어와도 0건 파싱**

```java
NodeList corpList = doc.getElementsByTagName("corp");
```

DART `corpCode.xml`의 실제 레코드는 `<corp>`가 아니라 다른 래퍼 요소를 쓴다.
태그 이름이 실제 데이터와 맞지 않아 `corpList.getLength()`는 0 — 예외 없이
조용히 빈 리스트를 반환한다.

**(c) 서비스 클래스를 MyBatis 매퍼로 오인**

```java
public void updateCompanies(List<String> companies) {
    ssf.openSession().getMapper(DisclosureService.class).updateCompanies(companies);
}
```

`getMapper()`는 `@Mapper` 인터페이스 + 매핑 XML이 있는 타입을 기대하는데, 여기
넘긴 `DisclosureService.class`는 일반 `@Service` 클래스다. 호출 시 런타임 예외.

**v2의 대응**: `CorpCodeZipParser`가 StAX로 ZIP 엔트리 바이트를 직접 스트리밍
파싱하고(문자열화 없음), 태그 이름을 하드코딩하지 않고 `corp_code`/`corp_name`/
`stock_code`/`modify_date` **필드명**만으로 레코드 경계를 판단하며, `JdbcTemplate.
batchUpdate`로 직접 DB에 적재한다(가짜 매퍼 경유 없음).

### 3. 인증이 서버 쪽에서 전혀 강제되지 않았다

```java
// @Configuration
// @EnableWebMvc
/*public class MkdpWebConfig implements WebMvcConfigurer {
    ...
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MkdpSessionInterceptor());
    }
}*/
```

`MkdpWebConfig` 클래스 전체가 주석 처리돼 `@Configuration`도 적용되지 않는다.
`MkdpSessionInterceptor`에 어떤 로직이 있든 스프링 빈으로 등록되지 않으므로
인터셉터 체인에 절대 걸리지 않는다 — 로그인 여부와 무관하게 모든 엔드포인트가
그대로 열려 있었다.

**v2의 대응**: MVP는 로그인 자체를 제공하지 않는 공개 데모로 범위를 좁혔다.
대신 쓰기 작업(기업 코드 동기화)만 `X-Sync-Token` 헤더로 보호한다.

### 4. 응답 객체의 setter가 죽어 있었다

```java
@Data
public class ResultVO {
    Object result;
    boolean success;

    public ResultVO(boolean success, Object result) {
        this.result = result;
        this.success = false;   // 파라미터를 무시하고 항상 false
    }

    public void setResult(List<MemberVO> memberInfo) { /* no-op */ }
    public void setSuccess(boolean b) { /* no-op */ }
}
```

Lombok `@Data`가 생성했을 `setResult`/`setSuccess`를 손으로 쓴 빈 오버라이드가
가려버렸고, 생성자마저 `success` 파라미터를 무시하고 무조건 `false`를 대입한다.
`new ResultVO(true, data)`로 호출해도 `success`는 항상 `false` — 로그인 성공
여부를 프론트에 정확히 전달할 방법이 없었다.

### 5. 시크릿이 소스에 그대로 커밋돼 있었다

- DART 인증키가 `BusinessController`에 상수로 하드코딩(`CERTIFICATION_KEY`)
- `env/system.properties`에 GCP MySQL 접속 정보(`34.64.82.250:3306`, `root`)가
  평문으로 커밋 — public 리포에 2년 넘게 노출된 상태

**v2의 대응**: DART 키는 `DartProperties`를 통해 환경변수(`DART_API_KEY`)로만
주입하고 소스에는 절대 넣지 않는다. DB 접속 정보도 배포 시 생성되는
`/etc/mkdp/mkdp.env`(운영 서버에만 존재, 리포에 커밋되지 않음)에서 온다.

## 요약

| 문제 | 성격 |
|------|------|
| 백테스트에 필요한 가격 데이터 소스 부재 | 설계 단계의 근본적 공백 (수정 불가) |
| InputStream 문자열화, 하드코딩된 XML 태그, 잘못된 매퍼 호출 | 구현 버그 (수정 가능) |
| 인터셉터 미등록으로 인증 미강제 | 구성 누락 |
| ResultVO 생성자/setter 오작동 | 구현 버그 |
| 시크릿 하드코딩 | 운영 관행 문제 |

v1이 멈춘 진짜 이유는 "어려워서"가 아니라, 핵심 기능(백테스트)이 DART 하나만으로는
애초에 완성될 수 없는 설계였기 때문이라고 판단한다. v2는 그 핵심 기능을 걷어내고,
DART가 실제로 답할 수 있는 질문(공시·재무 조회)에만 집중해 끝까지 동작하는
서비스로 다시 만들었다.

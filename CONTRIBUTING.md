# 기여 가이드

## 개발 환경

- JDK 17 (`backend/build.gradle.kts`의 toolchain 기준)
- Node.js 22 이상
- PostgreSQL 16 (선택) — 지정하지 않으면 H2 파일 DB(`backend/data/mkdp`)로 동작한다

```bash
# 백엔드
cd backend
./gradlew test
DART_API_KEY=<발급받은 키> ./gradlew bootRun

# 프론트엔드
cd frontend
npm install
npm run dev   # http://localhost:5173, /api는 :4180으로 프록시
```

`DART_API_KEY` 없이도 기동은 되지만 검색을 제외한 모든 API가 인증 오류로 실패한다.
최초 데이터 동기화 절차는 [README](README.md#최초-데이터-동기화)를 따른다.

## 브랜치

`main`에 직접 푸시하지 않는다. 다음 접두사로 브랜치를 만든다.

| 접두사 | 용도 |
|---|---|
| `feat/` | 기능 추가 |
| `fix/` | 버그 수정 |
| `refactor/` | 동작 변화 없는 구조 개선 |
| `perf/` | 성능 개선 |
| `docs/` | 문서 |
| `chore/` | 빌드·설정·잡무 |
| `security/` | 보안 |

## 커밋 메시지

타이틀과 본문 모두 한국어로 쓴다. 형식은 `<type>[(scope)]: <설명>`.

```
fix(discovery): 랭킹 캐시 키에 limit이 빠져 결과 개수가 고정되던 버그 수정

@Cacheable 키가 market/sort만 포함해, 먼저 캐시된 요청의 limit이
이후 다른 limit 요청에도 그대로 재사용되고 있었다.
```

- 타입: `feat` `fix` `refactor` `docs` `chore` `perf` `ci` `security`
- 타이틀은 70자 이내로, "무엇을"보다 "왜"가 드러나게 쓴다
- 본문에 변경 배경을 남긴다 (선택)
- 이모지는 쓰지 않는다

## Pull Request

- 제목과 본문 모두 한국어로 쓰고 `.github/PULL_REQUEST_TEMPLATE.md` 양식을 채운다
- 관련 이슈를 `Closes #12` 형태로 연결한다
- CI(백엔드 테스트, 프론트엔드 타입체크)가 통과해야 머지한다

## 코드 규칙

- 백엔드는 Kotlin, 프론트엔드는 TypeScript. 주석은 영어와 한국어 중 주변 코드에 맞춘다
- **DART 호출은 `DartClient`를 통해서만 한다.** 컨트롤러나 서비스에서 직접 호출하지 않는다
  (v1이 호출을 흩어놓고 status 검사를 빠뜨려 실패한 이력이 있다 — `docs/V1-RETROSPECTIVE.md`)
- **DB 스키마 변경은 새 Flyway 마이그레이션 파일로 추가한다.** 이미 배포된 `V*.sql`은 수정하지 않는다
- **시크릿을 소스에 넣지 않는다.** DART 키는 `DART_API_KEY`, DB 접속 정보와 동기화 토큰은
  운영 서버의 `/etc/mkdp/mkdp.env`에서 주입된다
- 외부 응답 형식에 의존하는 파서(`NaverPriceClient`, `CorpCodeZipParser`)를 고칠 때는
  해당 테스트의 고정 응답도 함께 갱신한다

## 이슈

버그는 `파일:줄`을 지목하고 재현 절차를 남긴다. 템플릿은 이슈 생성 시 자동으로 뜬다.

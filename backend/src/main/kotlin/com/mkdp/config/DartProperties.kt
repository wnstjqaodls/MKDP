package com.mkdp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * DART(금융감독원 전자공시시스템) Open API 접속 정보.
 *
 * 레거시(v1)는 인증키를 [com.mkdp] 소스에 상수로 박아 넣고 그대로 공개 저장소에 커밋했다.
 * v2는 환경변수(DART_API_KEY)로만 주입하고 소스에는 절대 값을 두지 않는다.
 */
@Configuration
@ConfigurationProperties(prefix = "dart")
class DartProperties {
    var apiKey: String = ""
    var baseUrl: String = "https://opendart.fss.or.kr/api"
}

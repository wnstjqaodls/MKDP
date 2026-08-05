package com.mkdp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mkdp")
class MkdpProperties {
    /** 기업 마스터(고유번호) 동기화 관리자 API 호출 토큰. 비어있으면 동기화 엔드포인트는 항상 거부한다. */
    var syncToken: String = ""
}

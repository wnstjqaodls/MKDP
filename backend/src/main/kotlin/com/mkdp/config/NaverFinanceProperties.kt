package com.mkdp.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * 네이버 금융 비공식 엔드포인트 접속 정보. DART는 일별 시세와 ETF 목록을 제공하지 않아
 * (v1이 백테스트를 완성하지 못한 근본 원인), 백테스트에 필요한 가격 데이터와 ETF
 * 유니버스는 별도로 네이버 금융에서 가져온다. 인증키가 필요 없는 공개 엔드포인트다.
 */
@Configuration
@ConfigurationProperties(prefix = "naver-finance")
class NaverFinanceProperties {
    var priceBaseUrl: String = "https://api.finance.naver.com"
    var etfListBaseUrl: String = "https://finance.naver.com"
}

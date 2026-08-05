package com.mkdp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfig {
    @Bean
    fun dartRestClient(properties: DartProperties): RestClient {
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5_000)
            setReadTimeout(15_000)
        }
        return RestClient.builder()
            .baseUrl(properties.baseUrl)
            .requestFactory(requestFactory)
            .build()
    }

    @Bean
    fun naverPriceRestClient(properties: NaverFinanceProperties): RestClient {
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5_000)
            setReadTimeout(15_000)
        }
        return RestClient.builder()
            .baseUrl(properties.priceBaseUrl)
            .requestFactory(requestFactory)
            .build()
    }

    @Bean
    fun naverEtfListRestClient(properties: NaverFinanceProperties): RestClient {
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(5_000)
            setReadTimeout(15_000)
        }
        return RestClient.builder()
            .baseUrl(properties.etfListBaseUrl)
            .requestFactory(requestFactory)
            .build()
    }
}

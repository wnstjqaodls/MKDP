package com.mkdp.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

/**
 * DART 호출은 일 20,000건 한도가 있고, 공개 데모라 남용 여지가 있다.
 * 상세 조회 3종(개황/공시목록/재무)을 캐싱해 동일 기업 재조회 시 DART를 다시 부르지 않는다.
 */
@Configuration
@EnableCaching
class CacheConfig {
    @Bean
    fun cacheManager(): CacheManager {
        val manager = CaffeineCacheManager()
        manager.registerCustomCache(
            "companyOverview",
            Caffeine.newBuilder().maximumSize(5_000).expireAfterWrite(Duration.ofHours(24)).build(),
        )
        manager.registerCustomCache(
            "financials",
            Caffeine.newBuilder().maximumSize(5_000).expireAfterWrite(Duration.ofHours(24)).build(),
        )
        manager.registerCustomCache(
            "disclosures",
            Caffeine.newBuilder().maximumSize(2_000).expireAfterWrite(Duration.ofMinutes(10)).build(),
        )
        manager.registerCustomCache(
            "discoveryRankings",
            Caffeine.newBuilder().maximumSize(100).expireAfterWrite(Duration.ofMinutes(20)).build(),
        )
        return manager
    }
}

package com.mkdp.api

import com.mkdp.config.MkdpProperties
import com.mkdp.domain.CorpCodeSyncService
import com.mkdp.domain.EtfSyncService
import com.mkdp.domain.SyncResult
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/** 기업/ETF 마스터 동기화 트리거. 최초 1회 수동 + 주 1회 systemd timer로 호출한다. */
@RestController
@RequestMapping("/api/admin")
class AdminSyncController(
    private val corpCodeSyncService: CorpCodeSyncService,
    private val etfSyncService: EtfSyncService,
    private val properties: MkdpProperties,
) {
    @PostMapping("/corp-codes/sync")
    fun syncCorpCodes(@RequestHeader("X-Sync-Token", required = false) token: String?): ResponseEntity<Any> =
        guarded(token) { corpCodeSyncService.sync() }

    @PostMapping("/etfs/sync")
    fun syncEtfs(@RequestHeader("X-Sync-Token", required = false) token: String?): ResponseEntity<Any> =
        guarded(token) { etfSyncService.sync() }

    private fun guarded(token: String?, action: () -> SyncResult): ResponseEntity<Any> {
        if (properties.syncToken.isBlank() || token != properties.syncToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(mapOf("error" to "인증 토큰이 올바르지 않습니다."))
        }
        val result = action()
        return if (result.success) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result)
        }
    }
}

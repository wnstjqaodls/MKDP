package com.mkdp.db

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class EtfSyncLogRepository(private val jdbc: NamedParameterJdbcTemplate) {
    fun start(id: String, startedAt: LocalDateTime) {
        jdbc.update(
            "INSERT INTO etf_sync_log (id, started_at, status) VALUES (:id, :startedAt, 'RUNNING')",
            mapOf("id" to id, "startedAt" to startedAt),
        )
    }

    fun finish(id: String, finishedAt: LocalDateTime, rowCount: Int, status: String) {
        jdbc.update(
            """
            UPDATE etf_sync_log
            SET finished_at = :finishedAt, row_count = :rowCount, status = :status
            WHERE id = :id
            """.trimIndent(),
            mapOf("id" to id, "finishedAt" to finishedAt, "rowCount" to rowCount, "status" to status),
        )
    }

    fun latest(): SyncLogRow? =
        jdbc.query(
            "SELECT id, started_at, finished_at, row_count, status FROM etf_sync_log ORDER BY started_at DESC LIMIT 1",
            emptyMap<String, Any>(),
        ) { rs, _ ->
            SyncLogRow(
                id = rs.getString("id"),
                startedAt = rs.localDateTime("started_at"),
                finishedAt = rs.localDateTimeOrNull("finished_at"),
                rowCount = rs.intOrNull("row_count"),
                status = rs.getString("status"),
            )
        }.firstOrNull()
}

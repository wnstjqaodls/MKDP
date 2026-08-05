package com.mkdp.db

import java.sql.ResultSet
import java.time.LocalDateTime

internal fun ResultSet.localDateTime(column: String): LocalDateTime =
    getTimestamp(column).toLocalDateTime()

internal fun ResultSet.localDateTimeOrNull(column: String): LocalDateTime? =
    getTimestamp(column)?.toLocalDateTime()

internal fun ResultSet.intOrNull(column: String): Int? {
    val value = getInt(column)
    return if (wasNull()) null else value
}

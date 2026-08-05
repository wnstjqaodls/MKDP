package com.mkdp.dart

import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CorpCodeZipParserTest {
    private val parser = CorpCodeZipParser()

    @Test
    fun `parses every record and preserves all four fields`() {
        val xml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <result>
                <list>
                    <corp_code>00126380</corp_code>
                    <corp_name>삼성전자</corp_name>
                    <corp_eng_name>Samsung Electronics</corp_eng_name>
                    <stock_code>005930</stock_code>
                    <modify_date>20260101</modify_date>
                </list>
                <list>
                    <corp_code>00164742</corp_code>
                    <corp_name>비상장기업</corp_name>
                    <corp_eng_name></corp_eng_name>
                    <stock_code></stock_code>
                    <modify_date>20251231</modify_date>
                </list>
            </result>
        """.trimIndent()

        val zip = zipOf("CORPCODE.xml" to xml)
        val records = mutableListOf<CorpCodeRecord>()

        val count = parser.parse(zip) { records.add(it) }

        assertEquals(2, count)
        assertEquals(2, records.size)

        val samsung = records[0]
        assertEquals("00126380", samsung.corpCode)
        assertEquals("삼성전자", samsung.corpName)
        assertEquals("005930", samsung.stockCode)
        assertEquals("20260101", samsung.modifyDate)

        val unlisted = records[1]
        assertEquals("00164742", unlisted.corpCode)
        assertNull(unlisted.stockCode)
    }

    @Test
    fun `ignores non-xml entries and returns zero for an empty archive`() {
        val zip = zipOf("readme.txt" to "not xml")
        val records = mutableListOf<CorpCodeRecord>()

        val count = parser.parse(zip) { records.add(it) }

        assertEquals(0, count)
    }

    private fun zipOf(vararg entries: Pair<String, String>): ByteArray {
        val buffer = ByteArrayOutputStream()
        ZipOutputStream(buffer).use { zos ->
            entries.forEach { (name, content) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(content.toByteArray(Charsets.UTF_8))
                zos.closeEntry()
            }
        }
        return buffer.toByteArray()
    }
}

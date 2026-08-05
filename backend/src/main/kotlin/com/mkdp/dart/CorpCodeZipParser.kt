package com.mkdp.dart

import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.zip.ZipInputStream
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLStreamConstants

/** 기업 고유번호 원본 레코드(코드/명/종목코드/최종변경일). */
data class CorpCodeRecord(
    val corpCode: String,
    val corpName: String,
    val stockCode: String?,
    val modifyDate: String,
)

/**
 * DART `corpCode.xml` 응답(ZIP 안에 든 XML, 약 10만 건)을 스트리밍으로 파싱한다.
 *
 * v1(레거시)의 버그 두 가지를 직접 겨냥해서 고친다:
 *  1. `String.valueOf(inputStream).getBytes()`로 InputStream 객체를 문자열화하던 버그
 *     → 여기서는 바이트를 직접 스트리밍 처리한다.
 *  2. `<corp>` 태그를 하드코딩해서 찾던 버그(실제 레코드 요소명과 불일치해 0건 파싱)
 *     → 요소명을 하드코딩하지 않고, corp_code/corp_name/stock_code/modify_date라는
 *        "필드명"만으로 레코드를 구성해 실제 래퍼 태그명(list 등)에 의존하지 않는다.
 */
@Component
class CorpCodeZipParser {
    private val fieldNames = setOf("corp_code", "corp_name", "stock_code", "modify_date")

    /** ZIP을 열어 안의 XML 엔트리를 파싱하고, 레코드마다 [onRecord]를 호출한다. 총 처리 건수를 반환한다. */
    fun parse(zipBytes: ByteArray, onRecord: (CorpCodeRecord) -> Unit): Int {
        var total = 0
        ZipInputStream(ByteArrayInputStream(zipBytes)).use { zis ->
            var entry = zis.nextEntry
            while (entry != null) {
                if (entry.name.endsWith(".xml", ignoreCase = true)) {
                    // 엔트리 바이트만 먼저 메모리로 떼어낸다. corpCode.xml 안에는 XML이 한 개뿐이라
                    // 부담이 크지 않고, StAX 리더가 EOF에서 기반 스트림을 닫아버려도
                    // (JDK 기본 구현의 알려진 동작) 공유 중인 zis에는 영향이 없다.
                    total += parseXml(ByteArrayInputStream(zis.readBytes()), onRecord)
                }
                entry = zis.nextEntry
            }
        }
        return total
    }

    private fun parseXml(input: InputStream, onRecord: (CorpCodeRecord) -> Unit): Int {
        val factory = XMLInputFactory.newInstance().apply {
            // XXE(외부 엔티티) 차단
            setProperty(XMLInputFactory.SUPPORT_DTD, false)
            setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false)
        }
        val reader = factory.createXMLStreamReader(input)
        var count = 0
        var currentField: String? = null
        val values = mutableMapOf<String, String>()
        val text = StringBuilder()

        fun flushIfPresent() {
            val corpCode = values["corp_code"] ?: return
            onRecord(
                CorpCodeRecord(
                    corpCode = corpCode,
                    corpName = values["corp_name"] ?: "",
                    stockCode = values["stock_code"]?.takeIf { it.isNotBlank() },
                    modifyDate = values["modify_date"] ?: "",
                ),
            )
            count++
            values.clear()
        }

        while (reader.hasNext()) {
            when (reader.next()) {
                XMLStreamConstants.START_ELEMENT -> {
                    val name = reader.localName
                    if (name in fieldNames) {
                        // corp_code가 다시 등장하면 이전 레코드가 끝난 것 — 태그 이름과 무관하게 경계를 판단
                        if (name == "corp_code" && values.containsKey("corp_code")) {
                            flushIfPresent()
                        }
                        currentField = name
                        text.setLength(0)
                    }
                }
                XMLStreamConstants.CHARACTERS, XMLStreamConstants.CDATA -> {
                    if (currentField != null) text.append(reader.text)
                }
                XMLStreamConstants.END_ELEMENT -> {
                    if (currentField != null && reader.localName == currentField) {
                        values[currentField] = text.toString().trim()
                        currentField = null
                    }
                }
                else -> Unit
            }
        }
        flushIfPresent()
        return count
    }
}

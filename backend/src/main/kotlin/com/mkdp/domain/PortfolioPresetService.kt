package com.mkdp.domain

import org.springframework.stereotype.Service

data class PresetHolding(val symbol: String, val weight: Double, val name: String, val type: AssetType)

data class PortfolioPreset(
    val key: String,
    val label: String,
    val description: String,
    val holdings: List<PresetHolding>,
)

private data class PresetDefinition(val key: String, val label: String, val description: String, val holdings: Map<String, Double>)

/**
 * 검색 없이 바로 고를 수 있는 큐레이션 포트폴리오 — 게임의 장비 추천처럼, 처음 온
 * 사용자가 "이 중 하나 골라서 바로 백테스트"할 수 있게 한다. 종목/비중은 코드에
 * 고정하고, 이름은 항상 최신 DB에서 조회해 채운다(리포에 시세를 커밋하지 않기 위함).
 */
@Service
class PortfolioPresetService(
    private val assetSearchService: AssetSearchService,
) {
    private val definitions = listOf(
        PresetDefinition(
            key = "conservative",
            label = "안정형",
            description = "채권·현금성 자산 위주로 변동성을 낮춘 구성",
            holdings = mapOf("459580" to 40.0, "273130" to 40.0, "069500" to 20.0),
        ),
        PresetDefinition(
            key = "balanced",
            label = "균형형",
            description = "국내·해외 주식과 채권을 고르게 섞은 구성",
            holdings = mapOf("069500" to 30.0, "360750" to 30.0, "273130" to 40.0),
        ),
        PresetDefinition(
            key = "aggressive",
            label = "공격형",
            description = "성장주·반도체 테마 위주로 높은 변동성을 감수하는 구성",
            holdings = mapOf("133690" to 35.0, "396500" to 35.0, "000660" to 30.0),
        ),
    )

    fun list(): List<PortfolioPreset> = definitions.map { def ->
        val holdings = def.holdings.mapNotNull { (symbol, weight) ->
            assetSearchService.resolve(symbol)?.let { asset ->
                PresetHolding(symbol = symbol, weight = weight, name = asset.name, type = asset.type)
            }
        }
        PortfolioPreset(key = def.key, label = def.label, description = def.description, holdings = holdings)
    }
}

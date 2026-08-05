package com.mkdp.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class BacktestResultVO {
    private BigDecimal finalAmount;          // 최종 금액
    private BigDecimal totalReturn;          // 총 수익률
    private BigDecimal annualizedReturn;     // 연간 수익률
    private BigDecimal maxDrawdown;          // 최대 낙폭
    private List<PortfolioValueVO> dailyValues; // 일별 포트폴리오 가치
} 
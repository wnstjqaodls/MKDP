package com.mkdp.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

@Data
public class PortfolioValueVO {
    private Date date;                   // 날짜
    private BigDecimal portfolioValue;   // 포트폴리오 가치
    private BigDecimal dailyReturn;      // 일간 수익률
    private Map<String, BigDecimal> assetValues; // 각 자산별 가치
} 
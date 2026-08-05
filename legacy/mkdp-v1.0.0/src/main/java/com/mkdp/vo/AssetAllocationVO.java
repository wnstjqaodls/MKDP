package com.mkdp.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AssetAllocationVO {
    private String companyCode;          // 기업 고유번호
    private String companyName;          // 기업명
    private BigDecimal allocationRatio;  // 배분 비율 (예: 0.4 = 40%)
    private BigDecimal currentValue;     // 현재 가치
} 
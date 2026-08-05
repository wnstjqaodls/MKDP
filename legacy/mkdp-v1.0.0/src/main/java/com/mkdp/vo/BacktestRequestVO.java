package com.mkdp.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class BacktestRequestVO {
    private Date startDate;          // 백테스트 시작일
    private Date endDate;            // 백테스트 종료일
    private BigDecimal initialAmount;// 초기 투자금액
    private List<AssetAllocationVO> allocations; // 자산 배분 정보 리스트
} 
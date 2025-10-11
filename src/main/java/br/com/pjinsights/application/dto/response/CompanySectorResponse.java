package br.com.pjinsights.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CompanySectorResponse {
    private BigDecimal averageInvoice;
    private BigDecimal averageSectorInvoice;
    private BigDecimal diffAverages;
    private Long sectorCompaniesAmount;
}

package br.com.santander.pjinsight.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyResponse {
    private String cnpj;
    private String name;
    private BigDecimal totalInvoicing;
    private BigDecimal totalBalance;
    private LocalDate openingDate;
    private String cnae;
    private String email;
    private String telephone;
    private String profile;
    private LocalDateTime classificationDate;
    private AddressResponse address;
    private List<BalanceResponse> balance;
    private List<InvoiceResponse> invoice;
    private BigDecimal averageMonthlyInvoice;
    private Double balanceGrowthLastFiveMonths;
    private Double transactionGrowthLastThreeMonths;
    private CompanySectorResponse companySectorResponse;
}


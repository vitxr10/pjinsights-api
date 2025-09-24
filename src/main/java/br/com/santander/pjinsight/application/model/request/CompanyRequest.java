package br.com.santander.pjinsight.application.model.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CompanyRequest {
    private String cnpj;
    private String name;
    private BigDecimal totalInvoicing;
    private BigDecimal totalBalance;
    private LocalDate openingDate;
    private String cnae;
    private String email;
    private String telephone;
    private AddressRequest address;
    private BalanceRequest balanceRequest;
    private InvoiceRequest invoiceRequest;
}


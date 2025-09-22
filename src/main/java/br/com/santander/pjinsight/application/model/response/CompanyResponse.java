package br.com.santander.pjinsight.application.model.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    private AddressResponse addressResponse;
}

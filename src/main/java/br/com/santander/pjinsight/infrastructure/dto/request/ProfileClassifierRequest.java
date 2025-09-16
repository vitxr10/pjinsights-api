package br.com.santander.pjinsight.infrastructure.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProfileClassifierRequest {

    @JsonProperty("ID")
    private String cnpj;

    @JsonProperty("VL_FATU")
    private BigDecimal totalInvoicing;

    @JsonProperty("VL_SLDO")
    private BigDecimal totalBalance;

    @JsonProperty("DT_ABRT")
    private String openingDate;

    @JsonProperty("DS_CNAE")
    private String cnae;
}





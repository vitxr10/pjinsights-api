package br.com.santander.pjinsight.infrastructure.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProfileClassifierResponse {

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

    @JsonProperty("PERFIL")
    private String profile;
}

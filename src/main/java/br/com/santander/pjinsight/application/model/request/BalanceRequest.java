package br.com.santander.pjinsight.application.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    private LocalDate referenceDate;
    private BigDecimal balanceValue;
}

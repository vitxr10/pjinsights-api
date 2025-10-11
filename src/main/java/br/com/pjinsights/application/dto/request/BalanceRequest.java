package br.com.pjinsights.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceRequest {
    private LocalDate referenceDate;
    private BigDecimal balanceValue;
}

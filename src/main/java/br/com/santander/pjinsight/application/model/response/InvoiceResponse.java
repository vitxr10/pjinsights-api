package br.com.santander.pjinsight.application.model.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceResponse {
    private LocalDate referenceDate;
    private BigDecimal invoiceValue;
}

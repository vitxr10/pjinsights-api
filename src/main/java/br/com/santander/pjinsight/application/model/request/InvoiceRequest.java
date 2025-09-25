package br.com.santander.pjinsight.application.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    @Min(1)
    @Max(12)
    private Short month;
    private BigDecimal invoiceValue;
}

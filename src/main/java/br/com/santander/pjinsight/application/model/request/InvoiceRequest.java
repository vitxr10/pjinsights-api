package br.com.santander.pjinsight.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceRequest {
    private String month;
    private BigDecimal invoiceValue;
}

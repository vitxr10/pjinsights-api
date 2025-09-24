package br.com.santander.pjinsight.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InvoiceResponse {
    private String month;
    private BigDecimal invoiceValue;
}

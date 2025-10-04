package br.com.santander.pjinsight.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {
    private UUID id;
    private LocalDate referenceDate;
    private BigDecimal amount;
    private String senderCnpj;
    private String receiverCnpj;
    private String paymentMethod;
}

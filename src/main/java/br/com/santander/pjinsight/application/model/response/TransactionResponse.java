package br.com.santander.pjinsight.application.model.response;

import br.com.santander.pjinsight.domain.enums.CategoryEnum;
import br.com.santander.pjinsight.domain.enums.PaymentMethodEnum;
import br.com.santander.pjinsight.domain.enums.StatusEnum;
import br.com.santander.pjinsight.domain.enums.TypeEnum;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionResponse {

    private UUID id;

    private LocalDateTime dateTime;

    private BigDecimal amount;

    private CategoryEnum category;

    private UUID senderId;

    private UUID receiverId;

    private BigDecimal previousBalance;

    private BigDecimal newBalance;

    private PaymentMethodEnum paymentMethod;

    private StatusEnum status;

    private TypeEnum type;

    private String description;
}

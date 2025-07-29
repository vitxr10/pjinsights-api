package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.CategoryEnum;
import br.com.santander.pjinsight.model.enums.PaymentMethodEnum;
import br.com.santander.pjinsight.model.enums.StatusEnum;
import br.com.santander.pjinsight.model.enums.TypeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    // rever algumas restricoes

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private CategoryEnum category;
    private UUID senderId;
    private UUID receiverId;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    @Enumerated(EnumType.STRING)
    private PaymentMethodEnum paymentMethod;
    @Enumerated(EnumType.STRING)
    private StatusEnum status;
    @Enumerated(EnumType.STRING)
    private TypeEnum type;
    private String description;



}

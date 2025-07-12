package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.CategoryEnum;
import br.com.santander.pjinsight.model.enums.PaymentMethodEnum;
import br.com.santander.pjinsight.model.enums.StatusEnum;
import br.com.santander.pjinsight.model.enums.TypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@Data
public class Transaction {

    // rever algumas restricoes

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime dateTime;
    private BigDecimal amount;
    private CategoryEnum category;
    private String senderId;
    private String receiverId;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private PaymentMethodEnum paymentMethod;
    private StatusEnum status;
    private TypeEnum type;
    private String description;



}

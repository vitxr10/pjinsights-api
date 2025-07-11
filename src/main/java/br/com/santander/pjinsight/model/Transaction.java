package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.Category;
import br.com.santander.pjinsight.model.enums.PaymentMethod;
import br.com.santander.pjinsight.model.enums.Status;
import br.com.santander.pjinsight.model.enums.Type;
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
    private Category category;
    private String senderId;
    private String receiverId;
    private BigDecimal previousBalance;
    private BigDecimal newBalance;
    private PaymentMethod paymentMethod;
    private Status status;
    private Type type;
    private String description;



}

package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.AccountType;
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
public class CompanyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private BigDecimal balance;
    private String agency;
    private String number;
    private String type;
    private String institution;
    private LocalDateTime openingDate;
    private String companyId;

}

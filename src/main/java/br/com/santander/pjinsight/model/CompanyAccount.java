package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.AccountTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CompanyAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private BigDecimal balance;
    private String agency;
    private String number;
    private AccountTypeEnum type;
    private String institution;
    private LocalDateTime openingDate;
    private UUID companyId;

}

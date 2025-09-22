package br.com.santander.pjinsight.application.model.response;

import br.com.santander.pjinsight.domain.enums.AccountTypeEnum;
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
public class CompanyAccountResponse {

    private UUID id;

    private BigDecimal balance;

    private String agency;

    private String number;

    private AccountTypeEnum type;

    private String institution;

    private LocalDateTime openingDate;

    private UUID companyId;
}

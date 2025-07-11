package br.com.santander.pjinsight.dto;

import br.com.santander.pjinsight.model.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyAccountDTO {

    @NotNull(message = "Saldo é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "Saldo deve ser maior ou igual a zero")
    private BigDecimal balance;

    @NotBlank(message = "Agência é obrigatória")
    @Size(max = 10, message = "Agência deve ter até 10 caracteres")
    private String agency;

    @NotBlank(message = "Número da conta é obrigatório")
    @Size(max = 20, message = "Número da conta deve ter até 20 caracteres")
    private String number;

    @NotNull(message = "Tipo de conta é obrigatório")
    private AccountType type;

    @NotBlank(message = "Instituição é obrigatória")
    @Size(max = 100, message = "Instituição deve ter até 100 caracteres")
    private String institution;

    @NotNull(message = "Data de abertura é obrigatória")
    @PastOrPresent(message = "Data de abertura não pode ser no futuro")
    private LocalDateTime openingDate;

    @NotBlank(message = "ID da empresa é obrigatório")
    @Pattern(
            regexp = "^[0-9a-fA-F\\-]{36}$",
            message = "ID da empresa deve ser um UUID válido"
    )
    private String companyId;
}

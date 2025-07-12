package br.com.santander.pjinsight.dto.req;

import br.com.santander.pjinsight.model.enums.RegistrationStatusEnum;
import br.com.santander.pjinsight.model.enums.SizeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CompanyRequest {

    private String id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter até 100 caracteres")
    private String name;

    @NotBlank(message = "CNPJ é obrigatório")
    @Pattern(
            regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$",
            message = "CNPJ deve seguir o formato 00.000.000/0000-00"
    )
    private String cnpj;

    @NotBlank(message = "Data de abertura é obrigatória")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2}$",
            message = "Data de abertura deve estar no formato YYYY-MM-DD"
    )
    private String pjOpeningDate;

    @NotBlank(message = "CNAE é obrigatório")
    @Size(min = 7, max = 7, message = "CNAE deve conter 7 caracteres")
    private String cnae;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Telefone é obrigatório")
    @Pattern(
            regexp = "\\+?[0-9]{8,15}",
            message = "Telefone deve conter apenas dígitos e ter entre 8 e 15 caracteres"
    )
    private String telephone;

    @NotNull(message = "Tamanho da empresa é obrigatório")
    private SizeEnum size;

    @NotNull(message = "Status de registro é obrigatório")
    private RegistrationStatusEnum registrationStatus;
}

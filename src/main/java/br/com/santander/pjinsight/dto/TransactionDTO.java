package br.com.santander.pjinsight.dto;

import br.com.santander.pjinsight.model.enums.Category;
import br.com.santander.pjinsight.model.enums.PaymentMethod;
import br.com.santander.pjinsight.model.enums.Status;
import br.com.santander.pjinsight.model.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {

    @NotNull(message = "Data e hora são obrigatórios")
    @PastOrPresent(message = "Data e hora não pode ser no futuro")
    private LocalDateTime dateTime;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.00", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    @NotNull(message = "Categoria é obrigatória")
    private Category category;

    @NotBlank(message = "ID do remetente é obrigatório")
    @Pattern(
            regexp = "^[0-9a-fA-F\\-]{36}$",
            message = "ID do remetente deve ser um UUID válido"
    )
    private String senderId;

    @NotBlank(message = "ID do destinatário é obrigatório")
    @Pattern(
            regexp = "^[0-9a-fA-F\\-]{36}$",
            message = "ID do destinatário deve ser um UUID válido"
    )
    private String receiverId;

    @NotNull(message = "Saldo anterior é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "Saldo anterior não pode ser negativo")
    private BigDecimal previousBalance;

    @NotNull(message = "Novo saldo é obrigatório")
    @DecimalMin(value = "0.00", inclusive = true, message = "Novo saldo não pode ser negativo")
    private BigDecimal newBalance;

    @NotNull(message = "Método de pagamento é obrigatório")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Status da transação é obrigatório")
    private Status status;

    @NotNull(message = "Tipo de transação é obrigatório")
    private Type type;

    @Size(max = 255, message = "Descrição deve ter até 255 caracteres")
    private String description;
}

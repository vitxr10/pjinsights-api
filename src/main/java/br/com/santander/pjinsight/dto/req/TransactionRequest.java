package br.com.santander.pjinsight.dto.req;

import br.com.santander.pjinsight.model.enums.CategoryEnum;
import br.com.santander.pjinsight.model.enums.PaymentMethodEnum;
import br.com.santander.pjinsight.model.enums.StatusEnum;
import br.com.santander.pjinsight.model.enums.TypeEnum;
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
public class TransactionRequest {

    private String id;

    @NotNull(message = "Data e hora são obrigatórios")
    @PastOrPresent(message = "Data e hora não pode ser no futuro")
    private LocalDateTime dateTime;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.00", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal amount;

    @NotNull(message = "Categoria é obrigatória")
    private CategoryEnum category;

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
    private PaymentMethodEnum paymentMethod;

    @NotNull(message = "Status da transação é obrigatório")
    private StatusEnum status;

    @NotNull(message = "Tipo de transação é obrigatório")
    private TypeEnum type;

    @Size(max = 255, message = "Descrição deve ter até 255 caracteres")
    private String description;
}

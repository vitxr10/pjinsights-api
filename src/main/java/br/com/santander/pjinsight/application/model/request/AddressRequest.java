package br.com.santander.pjinsight.application.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressRequest {

    private UUID id;

    @NotBlank(message = "Rua é obrigatória")
    @Size(max = 100, message = "Rua deve ter até 100 caracteres")
    private String street;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ser o código de 2 letras")
    private String state;

    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "CEP deve seguir o formato 12345-678")
    private String zipCode;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 50, message = "Cidade deve ter até 50 caracteres")
    private String city;

    @NotBlank(message = "País é obrigatório")
    @Size(max = 50, message = "País deve ter até 50 caracteres")
    private String country;

    @Size(max = 255, message = "Complemento deve ter até 255 caracteres")
    private String additionalAddressData;

    @NotBlank(message = "Número é obrigatório")
    @Size(max = 10, message = "Número deve ter até 10 caracteres")
    private String number;

}

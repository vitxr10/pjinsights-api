package br.com.pjinsights.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressResponse {
    private UUID id;
    private String street;
    private String state;
    private String zipCode;
    private String city;
    private String country;
    private String additionalAddressData;
    private String number;
}

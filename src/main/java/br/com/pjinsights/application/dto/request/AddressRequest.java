package br.com.pjinsights.application.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddressRequest {
    private String street;
    private String state;
    private String zipCode;
    private String city;
    private String country;
    private String additionalAddressData;
    private String number;
}

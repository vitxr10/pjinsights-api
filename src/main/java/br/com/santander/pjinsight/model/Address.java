package br.com.santander.pjinsight.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


@Entity
@AllArgsConstructor
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String street;
    private String state;
    private String zipCode;
    private String city;
    private String country;
    private String additionalAddressData;
    private String number;
    private String companyId;
}

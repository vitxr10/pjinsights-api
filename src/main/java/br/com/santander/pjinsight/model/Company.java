package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.RegistrationStatus;
import br.com.santander.pjinsight.model.enums.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;


import java.util.Set;

@Entity
@AllArgsConstructor
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String cnpj;

    private String pjOpeningDate;

    private String cnae;

    private String email;

    private String telephone;

    private Size size;

    private RegistrationStatus registrationStatus;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private Set<Address> addressList;


}


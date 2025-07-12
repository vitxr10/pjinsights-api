package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.RegistrationStatusEnum;
import br.com.santander.pjinsight.model.enums.SizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

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

    private SizeEnum size;

    private RegistrationStatusEnum registrationStatus;

//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//    private Set<Address> addressList;


}


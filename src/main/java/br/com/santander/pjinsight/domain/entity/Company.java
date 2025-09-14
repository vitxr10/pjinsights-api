package br.com.santander.pjinsight.domain.entity;

import br.com.santander.pjinsight.domain.enums.RegistrationStatusEnum;
import br.com.santander.pjinsight.domain.enums.SizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String cnpj;

    private String pjOpeningDate;

    private String cnae;

    private String email;

    private String telephone;

    @Enumerated(EnumType.STRING)
    private SizeEnum size;

    @Enumerated(EnumType.STRING)
    private RegistrationStatusEnum registrationStatus;

//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
//    private Set<Address> addressList;


}


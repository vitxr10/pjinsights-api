package br.com.santander.pjinsight.model;

import br.com.santander.pjinsight.model.enums.RegistrationStatus;
import br.com.santander.pjinsight.model.enums.Size;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String cnae;

    private String email;

    private String telephone;

    private Size size;

    private RegistrationStatus registrationStatus;


}


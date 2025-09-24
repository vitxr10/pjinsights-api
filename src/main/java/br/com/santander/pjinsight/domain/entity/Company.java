package br.com.santander.pjinsight.domain.entity;

import br.com.santander.pjinsight.domain.enums.RegistrationStatusEnum;
import br.com.santander.pjinsight.domain.enums.SizeEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String cnpj;
    private String name;
    private BigDecimal totalInvoicing;
    private BigDecimal totalBalance;
    private LocalDate openingDate;
    private String cnae;
    private String email;
    private String telephone;
    private String profile;
    private LocalDateTime classificationDate;
    private Double cresmentoSaldo5Meses; //TODO traduzir para ing
    private BigDecimal receitaMediaMensal; //TODO traduzir para ing
    private Double crescimentoTransacoes; //TODO traduzir para ing
}


package br.com.pjinsights.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate classificationDate;
}


package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.Company;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Company findByCnpj(String cnpj);

    List<Company> findByCnpjIn(List<String> cnpjs);
}

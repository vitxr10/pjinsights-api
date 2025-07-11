package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.Company;

public interface CompanyRepository extends JpaRepository<Company, String> {
    
}

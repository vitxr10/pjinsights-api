package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.CompanyAccount;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, String> {
    
}

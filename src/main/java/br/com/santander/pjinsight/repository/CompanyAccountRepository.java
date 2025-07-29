package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.CompanyAccount;

import java.util.UUID;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, UUID> {
    
}

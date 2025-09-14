package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.CompanyAccount;

import java.util.UUID;

public interface CompanyAccountRepository extends JpaRepository<CompanyAccount, UUID> {
    
}

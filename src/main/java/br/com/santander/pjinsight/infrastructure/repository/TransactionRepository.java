package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.Transaction;

import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    
}

package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    
}

package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findBySenderCnpjOrReceiverCnpj(String senderCnpj, String receiverCnpj);

    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE (t.senderCnpj = :cnpj OR t.receiverCnpj = :cnpj)
          AND FUNCTION('DATE_TRUNC', 'month', t.referenceDate) = (
              SELECT FUNCTION('DATE_TRUNC', 'month', MIN(t2.referenceDate))
              FROM Transaction t2
          )
    """)
    Integer countTransactionsFirstMonthByCnpj(@Param("cnpj") String cnpj);

    @Query("""
        SELECT COUNT(t)
        FROM Transaction t
        WHERE (t.senderCnpj = :cnpj OR t.receiverCnpj = :cnpj)
          AND FUNCTION('DATE_TRUNC', 'month', t.referenceDate) = (
              SELECT FUNCTION('DATE_TRUNC', 'month', MAX(t2.referenceDate))
              FROM Transaction t2
          )
    """)
    Integer countTransactionsLastMonthByCnpj(@Param("cnpj") String cnpj);
}

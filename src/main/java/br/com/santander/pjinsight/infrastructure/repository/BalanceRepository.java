package br.com.santander.pjinsight.infrastructure.repository;

import br.com.santander.pjinsight.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {
    List<Balance> findByCompanyId(UUID companyId);
}

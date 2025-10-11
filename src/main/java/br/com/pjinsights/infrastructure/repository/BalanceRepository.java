package br.com.pjinsights.infrastructure.repository;

import br.com.pjinsights.domain.entity.Balance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {
    List<Balance> findByCompanyId(UUID companyId);
}

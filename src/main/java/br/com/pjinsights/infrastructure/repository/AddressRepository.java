package br.com.pjinsights.infrastructure.repository;

import br.com.pjinsights.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    Address findByCompanyId(UUID companyId);
}

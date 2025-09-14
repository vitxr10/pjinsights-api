package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    
}

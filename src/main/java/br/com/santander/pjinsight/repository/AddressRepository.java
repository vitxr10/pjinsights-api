package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.Address;

import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {
    
}

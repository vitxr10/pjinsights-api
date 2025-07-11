package br.com.santander.pjinsight.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.model.Address;

public interface AddressRepository extends JpaRepository<Address, String> {
    
}

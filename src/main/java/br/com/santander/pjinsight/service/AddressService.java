package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.repository.AddressRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository repository;

}

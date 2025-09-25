package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.AddressRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.domain.entity.Address;
import br.com.santander.pjinsight.infrastructure.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AddressService {

    private final AddressRepository repository;
    private final ObjectMapper objectMapper;

    public AddressResponse save(AddressRequest addressRequest,UUID companyId){
        Address address = objectMapper.convertValue(addressRequest, Address.class);
        address.setCompanyId(companyId);
        repository.save(address);
        return objectMapper.convertValue(address, AddressResponse.class);
    }

    public AddressResponse findByCompanyId(UUID companyId) {
        AddressResponse addressResponse = new AddressResponse();
        Address address = repository.findByCompanyId(companyId);
        BeanUtils.copyProperties(address,addressResponse);
        return addressResponse;
    }
}

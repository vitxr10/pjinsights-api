package br.com.pjinsights.application.service;

import br.com.pjinsights.application.dto.request.AddressRequest;
import br.com.pjinsights.application.dto.response.AddressResponse;
import br.com.pjinsights.domain.entity.Address;
import br.com.pjinsights.infrastructure.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.AddressRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.domain.entity.Address;
import br.com.santander.pjinsight.infrastructure.repository.AddressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
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

    public AddressResponse findById(UUID id) {
        Address result = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return objectMapper.convertValue(result, AddressResponse.class);
    }

    public List<AddressResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(address -> objectMapper.convertValue(address, AddressResponse.class))
                .collect(Collectors.toList());
    }

    public AddressResponse update(AddressRequest addressRequest){
        Address address = repository.getReferenceById(addressRequest.getId());
        setAddress(address, addressRequest);
        return objectMapper.convertValue(address, AddressResponse.class);
    }

    public void deleteById(UUID id){
        Address address = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        objectMapper.convertValue(address, AddressResponse.class);
    }

    private void setAddress(Address address, AddressRequest addressRequest){
        address.setCountry(addressRequest.getCountry());
        address.setCity(addressRequest.getCity());
        address.setNumber(addressRequest.getNumber());
        address.setStreet(addressRequest.getStreet());
        address.setZipCode(addressRequest.getZipCode());
        address.setState(addressRequest.getState());

    }
}

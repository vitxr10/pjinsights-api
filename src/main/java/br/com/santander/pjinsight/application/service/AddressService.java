package br.com.santander.pjinsight.application.service;


import br.com.santander.pjinsight.application.model.request.AddressRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.domain.entity.Address;
import br.com.santander.pjinsight.infrastructure.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository repository;

    public AddressResponse save(AddressRequest addressRequest){
        Address address = repository.save(parseObject(addressRequest, Address.class));
        return parseObject(address, AddressResponse.class);
    }

    public AddressResponse findById(UUID id) {
        Address result = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return parseObject(result, AddressResponse.class);

    }

    public List<AddressResponse> findAll() {
        return parseListObjects(repository.findAll(), AddressResponse.class);
    }

    public AddressResponse update(AddressRequest addressRequest){
      Address address = repository.getReferenceById(addressRequest.getId());
      setAddress(address,addressRequest);
      return parseObject(address,AddressResponse.class);
    }

    public void deleteById(UUID id){
        Address address = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        parseObject(address, AddressResponse.class);
    }

    private void setAddress(Address address,AddressRequest addressRequest){
        address.setCountry(addressRequest.getCountry());
        address.setCity(addressRequest.getCity());
        address.setNumber(addressRequest.getNumber());
        address.setStreet(addressRequest.getStreet());
        address.setZipCode(addressRequest.getZipCode());
        address.setState(addressRequest.getState());
        address.setCompanyId(addressRequest.getCompanyId());
    }
}

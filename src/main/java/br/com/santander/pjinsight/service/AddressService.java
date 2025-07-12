package br.com.santander.pjinsight.service;


import br.com.santander.pjinsight.dto.req.AddressRequest;
import br.com.santander.pjinsight.dto.res.AddressResponse;
import br.com.santander.pjinsight.model.Address;
import br.com.santander.pjinsight.repository.AddressRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
@AllArgsConstructor
public class AddressService {

    private AddressRepository repository;

    public AddressResponse save(AddressRequest addressRequest){
        Address address = repository.save(parseObject(addressRequest, Address.class));
        return parseObject(address, AddressResponse.class);
    }

    public AddressResponse findById(String id) {
        Address result = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return parseObject(result, AddressResponse.class);

    }

    public List<AddressRequest> findAll() {
        return parseListObjects(repository.findAll(), AddressRequest.class);
    }

    public AddressResponse update(AddressRequest addressRequest){
      Address address = repository.getReferenceById(addressRequest.getId());
      setAddress(address,addressRequest);
      return parseObject(address,AddressResponse.class);
    }

    public AddressResponse deleteById(String id){
        Address address = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return parseObject(address,AddressResponse.class);
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

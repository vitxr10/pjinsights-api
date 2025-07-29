package br.com.santander.pjinsight.controller;

import br.com.santander.pjinsight.dto.req.AddressRequest;
import br.com.santander.pjinsight.dto.res.AddressResponse;
import br.com.santander.pjinsight.service.AddressService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll() {
        var list = addressService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable UUID id) {
        var address = addressService.findById(id);
        return new ResponseEntity<>(address, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AddressResponse> insert(@RequestBody @Valid AddressRequest request) {
        var address = addressService.save(request);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        addressService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<AddressResponse> update(@RequestBody @Valid AddressRequest request) {
        var address = addressService.update(request);
        return new ResponseEntity<>(address, HttpStatus.MOVED_PERMANENTLY);
    }
}

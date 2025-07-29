package br.com.santander.pjinsight.controller;

import br.com.santander.pjinsight.dto.req.CompanyAccountRequest;
import br.com.santander.pjinsight.dto.res.CompanyAccountResponse;
import br.com.santander.pjinsight.service.CompanyAccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/company-account")
public class CompanyAccountController {

    private final CompanyAccountService companyAccountService;

    @GetMapping
    public ResponseEntity<List<CompanyAccountResponse>> findAll() {
        var list = companyAccountService.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyAccountResponse> findById(@PathVariable UUID id) {
        var acc = companyAccountService.findById(id);
        return new ResponseEntity<>(acc, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CompanyAccountResponse> insert(@RequestBody @Valid CompanyAccountRequest request) {
        var acc = companyAccountService.save(request);
        return new ResponseEntity<>(acc, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyAccountService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<CompanyAccountResponse> update(@RequestBody @Valid CompanyAccountRequest request) {
        var acc = companyAccountService.update(request);
        return new ResponseEntity<>(acc, HttpStatus.MOVED_PERMANENTLY);
    }
}

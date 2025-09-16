package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.CompanyResponse;
import br.com.santander.pjinsight.application.service.CompanyService;
import br.com.santander.pjinsight.infrastructure.service.profileclassifier.ProfileClassifierService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    @GetMapping
    public ResponseEntity<List<CompanyResponse>> findAll(){
        var companyResponse = companyService.findAll();
        return new ResponseEntity<List<CompanyResponse>>(companyResponse,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> findById(@PathVariable UUID id){
        var company = companyService.findById(id);
        return new ResponseEntity<>(company, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> insert(@RequestBody CompanyRequest request) {
        var company = companyService.save(request);
        return new ResponseEntity<>(company,HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        companyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{cnpj}")
    public ResponseEntity<Void> classifyCompany(@PathVariable("cnpj") String cnpj){
        companyService.classifyCompany(cnpj);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@RequestBody @Valid CompanyRequest request, @PathVariable("id") UUID companyId) {
        var entity = companyService.update(request,companyId);
        return new ResponseEntity<>(entity, HttpStatus.OK);

    }


}

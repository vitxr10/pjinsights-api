package br.com.pjinsights.api.controller;

import br.com.pjinsights.application.dto.request.CompanyRequest;
import br.com.pjinsights.application.dto.response.CompanyResponse;
import br.com.pjinsights.application.dto.response.CompanySectorResponse;
import br.com.pjinsights.application.service.CompanyService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping()
    public ResponseEntity<Page<CompanyResponse>> findAll(@RequestParam(value = "page", required = false, defaultValue = "0")Integer page){
        var companyResponse = companyService.findAll(page);
        return new ResponseEntity<>(companyResponse,HttpStatus.OK);
    }

    @GetMapping("/classified")
    public ResponseEntity<Page<CompanyResponse>> findAllClassified(@RequestParam(value = "page", required = false, defaultValue = "0") Integer page) {
        var companyResponse = companyService.findAllClassifiedCompanies(page);
        return ResponseEntity.status(HttpStatus.OK).body(companyResponse);
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

    @PatchMapping("/classify/{cnpj}")
    public ResponseEntity<Void> classify(@PathVariable("cnpj") String cnpj){
        companyService.classifyCompanies(List.of(cnpj));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/cnpj/{cnpj}")
    public ResponseEntity<CompanyResponse> findCompanyByCnpj(@PathVariable("cnpj") String cnpj){
        CompanyResponse companyResponse = companyService.findByCnpj(cnpj);
        return ResponseEntity.status(HttpStatus.OK).body(companyResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@RequestBody @Valid CompanyRequest request, @PathVariable("id") UUID companyId) {
        var entity = companyService.update(request,companyId);
        return new ResponseEntity<>(entity, HttpStatus.OK);

    }

    @GetMapping("/sector-dash/{cnpj}")
    public ResponseEntity<CompanySectorResponse> getSectorDashboardData(@PathVariable("cnpj") String cnpj){
        CompanySectorResponse companySectorResponse = companyService.getSectorDashboardData(cnpj);
        return ResponseEntity.status(HttpStatus.OK).body(companySectorResponse);
    }
 }

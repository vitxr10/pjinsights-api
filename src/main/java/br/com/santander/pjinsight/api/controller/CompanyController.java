package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.CompanyResponse;
import br.com.santander.pjinsight.application.service.CompanyService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private CompanyService companyService;

    @GetMapping()
    public ResponseEntity<Page<CompanyResponse>> findAll(@RequestParam("page")Integer page){
        var companyResponse = companyService.findAll(page);
        return new ResponseEntity<>(companyResponse,HttpStatus.OK);
    }

    @GetMapping("/classified")
    public ResponseEntity<Page<CompanyResponse>> findAllClassified(@RequestParam("page")Integer page){
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

    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> update(@RequestBody @Valid CompanyRequest request, @PathVariable("id") UUID companyId) {
        var entity = companyService.update(request,companyId);
        return new ResponseEntity<>(entity, HttpStatus.OK);

    }

    @PostMapping(
            value = "/classify/batch",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> classifyBatch(
            @Parameter(description = "Arquivo CSV ou Excel contendo os CNPJs",
            content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = @Schema(type = "string", format = "binary")))
            @RequestPart("file") MultipartFile file
    ) {
        companyService.classifyBatch(file);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

}}

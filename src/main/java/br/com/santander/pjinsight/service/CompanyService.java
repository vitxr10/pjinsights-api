package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.dto.req.CompanyRequest;
import br.com.santander.pjinsight.dto.res.CompanyResponse;
import br.com.santander.pjinsight.model.Company;
import br.com.santander.pjinsight.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;

    public CompanyResponse save(CompanyRequest companyRequest) {
        Company company = repository.save(parseObject(companyRequest, Company.class));
        return parseObject(company, CompanyResponse.class);
    }

    public CompanyResponse findById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return parseObject(company, CompanyResponse.class);
    }

    public List<CompanyResponse> findAll() {
        return parseListObjects(repository.findAll(), CompanyResponse.class);
    }

    public CompanyResponse update(CompanyRequest companyRequest) {
        Company company = repository.getReferenceById(companyRequest.getId());
        setCompany(company, companyRequest);
        return parseObject(company, CompanyResponse.class);
    }

    public CompanyResponse deleteById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return parseObject(company, CompanyResponse.class);
    }

    private void setCompany(Company company, CompanyRequest req) {
        company.setName(req.getName());
        company.setCnpj(req.getCnpj());
        company.setPjOpeningDate(req.getPjOpeningDate());
        company.setCnae(req.getCnae());
        company.setEmail(req.getEmail());
        company.setTelephone(req.getTelephone());
        company.setSize(req.getSize());
        company.setRegistrationStatus(req.getRegistrationStatus());
    }
}

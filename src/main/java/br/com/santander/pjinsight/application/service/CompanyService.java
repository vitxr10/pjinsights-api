package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.CompanyResponse;
import br.com.santander.pjinsight.domain.entity.Company;
import br.com.santander.pjinsight.infrastructure.dto.request.ProfileClassifierRequest;
import br.com.santander.pjinsight.infrastructure.repository.CompanyRepository;
import br.com.santander.pjinsight.infrastructure.service.profileclassifier.ProfileClassifierService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;

    @Transactional
    public CompanyResponse save(CompanyRequest companyRequest) {
        Company company = objectMapper.convertValue(companyRequest, Company.class);
        company = repository.save(company);
        addressService.save(companyRequest.getAddress(),company.getId());
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    public CompanyResponse findById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    public Page<CompanyResponse> findAll(Integer page) {
        var pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable)
                .map(company -> {
                    var companyResponse = new CompanyResponse();
                    BeanUtils.copyProperties(company, companyResponse);
                    return companyResponse;
                });
    }

    @Transactional
    public CompanyResponse update(CompanyRequest companyRequest,UUID companyId) {
        Company company = repository.getReferenceById(companyId);
        setCompany(company, companyRequest);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    @Transactional
    public void deleteById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        objectMapper.convertValue(company, CompanyResponse.class);
    }

    private void setCompany(Company company, CompanyRequest req) {
        BeanUtils.copyProperties(req,company);
    }

    @Transactional
    public void classifyCompanies(List<String> cnpjs) {
        var companies = repository.findByCnpjIn(cnpjs);

        var requests = companies.stream()
                .map(company -> {
                    var request = new ProfileClassifierRequest();
                    BeanUtils.copyProperties(company, request);
                    request.setOpeningDate(company.getOpeningDate().toString());
                    return request;
                })
                .toList();

        var responses = profileClassifierService.classifyProfile(requests);

        for (int i = 0; i < companies.size(); i++) {
            var company = companies.get(i);
            var response = responses.get(i);
            company.setProfile(response.getProfile());
            company.setClassificationDate(LocalDateTime.now());
        }
        repository.saveAll(companies);
    }

    public Page<CompanyResponse> findAllClassifiedCompanies(Integer page) {
        var pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC,"classificationDate"));

        return repository.findAllClassified(pageable)
                .map(this::toResponse);
    }

    private CompanyResponse toResponse(Company company) {
        var response = new CompanyResponse();
        BeanUtils.copyProperties(company, response);
        return response;
    }

    public CompanyResponse findByCnpj(String cnpj) {
        Company company = repository.findByCnpj(cnpj);
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company,companyResponse);
        return companyResponse;
    }
}

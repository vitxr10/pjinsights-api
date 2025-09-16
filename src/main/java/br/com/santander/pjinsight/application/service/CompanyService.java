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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper; // Jackson sendo injetado

    @Transactional
    public CompanyResponse save(CompanyRequest CompanyRequest) {
        Company company = objectMapper.convertValue(CompanyRequest, Company.class);
        repository.save(company);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    public CompanyResponse findById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    public List<CompanyResponse> findAll() {
        return repository.findAll().stream()
                .map(company -> objectMapper.convertValue(company, CompanyResponse.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public CompanyResponse update(CompanyRequest companyRequest,UUID companyId) {
        Company company = repository.getReferenceById(companyId);
        setCompany(company, companyRequest);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    @Transactional
    public CompanyResponse deleteById(UUID id) {
        Company company = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return objectMapper.convertValue(company, CompanyResponse.class);
    }

    private void setCompany(Company company, CompanyRequest req) {
        BeanUtils.copyProperties(req,company);
    }

    @Transactional
    public void classifyCompany(String cnpj) {
        var company = repository.findByCnpj(cnpj);
        var profileClassifierRequest = new ProfileClassifierRequest();
        BeanUtils.copyProperties(company,profileClassifierRequest);
        profileClassifierRequest.setOpeningDate(company.getOpeningDate().toString());
        var profileClassifierRequestList = new ArrayList<ProfileClassifierRequest>();
        profileClassifierRequestList.add(profileClassifierRequest);
        var profileClassifierResponse = profileClassifierService.classifyText(profileClassifierRequestList);
        company.setProfile(profileClassifierResponse.get(0).getProfile());
    }
}

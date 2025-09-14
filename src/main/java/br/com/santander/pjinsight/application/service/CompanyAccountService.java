package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.CompanyAccountRequest;
import br.com.santander.pjinsight.application.model.response.CompanyAccountResponse;
import br.com.santander.pjinsight.domain.entity.CompanyAccount;
import br.com.santander.pjinsight.infrastructure.repository.CompanyAccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyAccountService {

    private final CompanyAccountRepository repository;
    private final ObjectMapper objectMapper;

    public CompanyAccountResponse save(CompanyAccountRequest req) {
        CompanyAccount account = objectMapper.convertValue(req, CompanyAccount.class);
        repository.save(account);
        return objectMapper.convertValue(account, CompanyAccountResponse.class);
    }

    public CompanyAccountResponse findById(UUID id) {
        CompanyAccount account = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return objectMapper.convertValue(account, CompanyAccountResponse.class);
    }

    public List<CompanyAccountResponse> findAll() {
        return repository.findAll().stream()
                .map(account -> objectMapper.convertValue(account, CompanyAccountResponse.class))
                .collect(Collectors.toList());
    }

    public CompanyAccountResponse update(CompanyAccountRequest req) {
        CompanyAccount account = repository.getReferenceById(req.getId());
        setCompanyAccount(account, req);
        return objectMapper.convertValue(account, CompanyAccountResponse.class);
    }

    public CompanyAccountResponse deleteById(UUID id) {
        CompanyAccount account = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return objectMapper.convertValue(account, CompanyAccountResponse.class);
    }

    private void setCompanyAccount(CompanyAccount acc, CompanyAccountRequest req) {
        acc.setBalance(req.getBalance());
        acc.setAgency(req.getAgency());
        acc.setNumber(req.getNumber());
        acc.setType(req.getType());
        acc.setInstitution(req.getInstitution());
        acc.setOpeningDate(req.getOpeningDate());
        acc.setCompanyId(req.getCompanyId());
    }
}

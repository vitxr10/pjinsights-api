package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.dto.req.CompanyAccountRequest;
import br.com.santander.pjinsight.dto.res.CompanyAccountResponse;
import br.com.santander.pjinsight.model.CompanyAccount;
import br.com.santander.pjinsight.repository.CompanyAccountRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
@AllArgsConstructor
public class CompanyAccountService {

    private final CompanyAccountRepository repository;

    public CompanyAccountResponse save(CompanyAccountRequest req) {
        CompanyAccount account = repository.save(parseObject(req, CompanyAccount.class));
        return parseObject(account, CompanyAccountResponse.class);
    }

    public CompanyAccountResponse findById(String id) {
        CompanyAccount account = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        return parseObject(account, CompanyAccountResponse.class);
    }

    public List<CompanyAccountRequest> findAll() {
        return parseListObjects(repository.findAll(), CompanyAccountRequest.class);
    }

    public CompanyAccountResponse update(CompanyAccountRequest req) {
        CompanyAccount account = repository.getReferenceById(req.getId());
        setCompanyAccount(account, req);
        return parseObject(account, CompanyAccountResponse.class);
    }

    public CompanyAccountResponse deleteById(String id) {
        CompanyAccount account = repository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        repository.deleteById(id);
        return parseObject(account, CompanyAccountResponse.class);
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

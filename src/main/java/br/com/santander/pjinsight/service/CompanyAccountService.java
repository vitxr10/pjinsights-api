package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.repository.CompanyAccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyAccountService {

    private CompanyAccountRepository companyAccountRepository;
}

package br.com.santander.pjinsight.service;

import br.com.santander.pjinsight.repository.CompanyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyService {

    private CompanyRepository companyRepository;


}

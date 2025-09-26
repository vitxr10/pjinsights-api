package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.application.model.response.BalanceResponse;
import br.com.santander.pjinsight.application.model.response.CompanyResponse;
import br.com.santander.pjinsight.application.model.response.InvoiceResponse;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;
    private final BalanceService balanceService;
    private final InvoiceService invoiceService;

    @Transactional
    public CompanyResponse save(CompanyRequest companyRequest) {
        Company company = objectMapper.convertValue(companyRequest, Company.class);
        company = repository.save(company);
        List<BalanceResponse> balanceResponse = balanceService.save(companyRequest.getBalance(),company.getId());
        List<InvoiceResponse> invoiceResponse = invoiceService.save(companyRequest.getInvoice(),company.getId());
        AddressResponse addressResponse = addressService.save(companyRequest.getAddress(),company.getId());
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company,companyResponse);
        companyResponse.setAddress(addressResponse);
        companyResponse.setInvoice(invoiceResponse);
        companyResponse.setBalance(balanceResponse);
        return companyResponse;
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
        CompanyResponse companyResponse = toResponse(company);

        companyResponse.setAddress(addressService.findByCompanyId(company.getId()));
        var invoices = invoiceService.findByCompanyId(company.getId());
        var balances = balanceService.findByCompanyId(company.getId());

        companyResponse.setInvoice(invoices);
        companyResponse.setBalance(balances);

        companyResponse.setAverageMonthlyInvoice(getAverageMonthlyInvoice(invoices));
        companyResponse.setBalanceGrowthLastFiveMonths(getBalanceGrowthLastFiveMonths(balances));
        companyResponse.setTransactionGrowthLastThreeMonths(getTransactionGrowthLastThreeMonths(invoices));

        return companyResponse;
    }

    public BigDecimal getAverageMonthlyInvoice(List<InvoiceResponse> invoices) {
        if (invoices == null || invoices.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = invoices.stream()
                .map(InvoiceResponse::getInvoiceValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(invoices.size()), 2, RoundingMode.HALF_UP);
    }

    public Double getBalanceGrowthLastFiveMonths(List<BalanceResponse> balances) {
        if (balances == null || balances.size() < 5) return 0.0;

        List<BalanceResponse> sorted = new ArrayList<>(balances);
        sorted.sort(Comparator.comparing(BalanceResponse::getMonth));

        BigDecimal fiveMonthsAgo = sorted.get(sorted.size() - 5).getBalanceValue();
        BigDecimal current = sorted.get(sorted.size() - 1).getBalanceValue();

        if (fiveMonthsAgo.compareTo(BigDecimal.ZERO) == 0) return 0.0;

        return current.subtract(fiveMonthsAgo)
                .divide(fiveMonthsAgo, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }


    public Double getTransactionGrowthLastThreeMonths(List<InvoiceResponse> invoices) {
        if (invoices == null || invoices.size() < 3) return 0.0;

        Map<Short, Long> txByMonth = invoices.stream()
                .collect(Collectors.groupingBy(InvoiceResponse::getMonth, Collectors.counting()));

        List<Short> months = new ArrayList<>(txByMonth.keySet());
        Collections.sort(months);

        if (months.size() < 3) return 0.0;

        Long past = txByMonth.get(months.get(months.size() - 3));
        Long current = txByMonth.get(months.get(months.size() - 1));

        if (past == null || past == 0) return 0.0;

        return ((double) (current - past) / past) * 100;
    }
}

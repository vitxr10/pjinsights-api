package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.CompanyRequest;
import br.com.santander.pjinsight.application.model.response.*;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;
    private final BalanceService balanceService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;

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
            company.setClassificationDate(LocalDate.now());
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

        CompanySectorResponse companySectorResponse = CompanySectorResponse.builder().
                                                      averageSectorInvoice(getAverageOtherCompaniesInvoice(cnpj)).
                                                      sectorCompaniesAmount(getSectorCompaniesAmount(company.getCnae())).
                                                      diffAverages(getAverageCompaniesInvoiceDiff(invoices,cnpj)).
                                                      averageInvoice(getAverageMonthlyInvoice(invoices))
                                                       .build();

        companyResponse.setInvoice(invoices);
        companyResponse.setBalance(balances);
        companyResponse.setAverageMonthlyInvoice(getAverageMonthlyInvoice(invoices));
        companyResponse.setBalanceGrowthLastFiveMonths(getBalanceGrowthLastFiveMonths(balances));
        companyResponse.setTransactionGrowthLastThreeMonths(getTransactionGrowthByCnpj(cnpj));
//        companyResponse.getCompanySectorResponse().setSectorCompaniesAmount(getSectorCompaniesAmount(company.getCnae()));
//        companyResponse.getCompanySectorResponse().setAverageSectorInvoice(getAverageOtherCompaniesInvoice(cnpj));
//        companyResponse.getCompanySectorResponse().setDiffAverages(getAverageCompaniesInvoiceDiff(invoices,cnpj));
//        companyResponse.getCompanySectorResponse().setAverageInvoice(getAverageMonthlyInvoice(invoices));
        companyResponse.setCompanySectorResponse(companySectorResponse);
        return companyResponse;
    }

    public BigDecimal getAverageMonthlyInvoice(List<InvoiceResponse> invoices) {
        if (invoices == null || invoices.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = invoices.stream()
                .map(InvoiceResponse::getInvoiceValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(invoices.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageOtherCompaniesInvoice(String cnpj) {
        // 1️⃣ Busca a empresa pelo CNPJ
        Company company = repository.findByCnpj(cnpj);
        if (company == null || company.getCnae() == null) {
            return BigDecimal.ZERO;
        }

        // 2️⃣ Busca todas as empresas do mesmo setor (CNAE)
        List<Company> sameSectorCompanies = repository.findByCnae(company.getCnae())
                .stream()
                .filter(c -> !c.getCnpj().equals(cnpj))  // remove a própria empresa
                .toList();

        if (sameSectorCompanies.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 3️⃣ Busca todas as faturas de cada empresa e calcula a média mensal de faturamento
        List<BigDecimal> averages = sameSectorCompanies.stream()
                .map(c -> {
                    List<InvoiceResponse> invoices = invoiceService.findByCompanyId(c.getId());
                    return getAverageMonthlyInvoice(invoices);
                })
                .filter(avg -> avg.compareTo(BigDecimal.ZERO) > 0)
                .toList();

        if (averages.isEmpty()) {
            return BigDecimal.ZERO;
        }

        // 4️⃣ Calcula a média geral entre todas as empresas do mesmo setor
        BigDecimal total = averages.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(averages.size()), 2, RoundingMode.HALF_UP);
    }

    public BigDecimal getAverageCompaniesInvoiceDiff(List<InvoiceResponse> invoices, String cnpj) {
        BigDecimal companyAverage = getAverageMonthlyInvoice(invoices);
        BigDecimal othersAverage = getAverageOtherCompaniesInvoice(cnpj);

        return companyAverage.subtract(othersAverage).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getSectorCompaniesAmount(String cnae){
        return repository.countByCnae(cnae);
    }

    public Double getBalanceGrowthLastFiveMonths(List<BalanceResponse> balances) {
        if (balances == null || balances.size() < 5) return 0.0;

        List<BalanceResponse> sorted = new ArrayList<>(balances);
        sorted.sort(Comparator.comparing(BalanceResponse::getReferenceDate));

        BigDecimal fiveMonthsAgo = sorted.get(sorted.size() - 5).getBalanceValue();
        BigDecimal current = sorted.get(sorted.size() - 1).getBalanceValue();

        if (fiveMonthsAgo.compareTo(BigDecimal.ZERO) == 0) return 0.0;

        return current.subtract(fiveMonthsAgo)
                .divide(fiveMonthsAgo, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .doubleValue();
    }

    public Double getTransactionGrowthByCnpj(String cnpj) {
        Integer firstMonthCount = transactionService.countTransactionsFirstMonthByCnpj(cnpj);
        Integer lastMonthCount = transactionService.countTransactionsLastMonthByCnpj(cnpj);

        if (firstMonthCount == null || lastMonthCount == null || firstMonthCount == 0) {
            return 0.0;
        }

        double growth = ((double) (lastMonthCount - firstMonthCount) / firstMonthCount) * 100;
        return Math.round(growth * 100.0) / 100.0;
    }

}

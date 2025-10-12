package br.com.pjinsights.application.service;

import br.com.pjinsights.application.dto.request.CompanyRequest;
import br.com.pjinsights.application.dto.response.*;
import br.com.pjinsights.domain.entity.Company;
import br.com.pjinsights.infrastructure.dto.request.ProfileClassifierRequest;
import br.com.pjinsights.infrastructure.repository.CompanyRepository;
import br.com.pjinsights.infrastructure.service.profileclassifier.ProfileClassifierService;
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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private final ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper;
    private final AddressService addressService;
    private final BalanceService balanceService;
    private final InvoiceService invoiceService;
    private final TransactionService transactionService;

    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);

    @Transactional
    public CompanyResponse save(CompanyRequest companyRequest) {
        Company company = objectMapper.convertValue(companyRequest, Company.class);
        repository.save(company);

        var balanceResponse = balanceService.save(companyRequest.getBalance(), company.getId());
        var invoiceResponse = invoiceService.save(companyRequest.getInvoice(), company.getId());
        var addressResponse = addressService.save(companyRequest.getAddress(), company.getId());

        var response = new CompanyResponse();

        BeanUtils.copyProperties(company, response);
        response.setBalance(balanceResponse);
        response.setInvoice(invoiceResponse);
        response.setAddress(addressResponse);

        return response;
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
    }

    private void setCompany(Company company, CompanyRequest req) {
        BeanUtils.copyProperties(req,company);
    }

    @Transactional
    public void classify(List<String> cnpjs) {
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

    public Page<CompanyResponse> findAllClassified(Integer page) {
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
        if (company == null) throw new EntityNotFoundException("Empresa não encontrada");

        CompletableFuture<List<InvoiceResponse>> invoicesFuture = CompletableFuture.supplyAsync(
                () -> invoiceService.findByCompanyId(company.getId()), EXECUTOR);
        CompletableFuture<List<BalanceResponse>> balancesFuture = CompletableFuture.supplyAsync(
                () -> balanceService.findByCompanyId(company.getId()), EXECUTOR);
        CompletableFuture<AddressResponse> addressFuture = CompletableFuture.supplyAsync(
                () -> addressService.findByCompanyId(company.getId()), EXECUTOR);

        List<InvoiceResponse> invoices = invoicesFuture.join();
        List<BalanceResponse> balances = balancesFuture.join();
        AddressResponse address = addressFuture.join();

        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company, companyResponse);
        companyResponse.setInvoice(invoices);
        companyResponse.setBalance(balances);
        companyResponse.setAddress(address);

        companyResponse.setAverageMonthlyInvoice(invoiceService.getAverageMonthlyInvoice(invoices));
        companyResponse.setBalanceGrowthLastFiveMonths(balanceService.getBalanceGrowthLastFiveMonths(balances));
        companyResponse.setTransactionGrowthLastThreeMonths(transactionService.getTransactionGrowthByCnpj(cnpj));

        return companyResponse;
    }

    public CompanySectorResponse getSectorDashboardData(String cnpj){
        Company company = repository.findByCnpj(cnpj);
        if (company == null) throw new EntityNotFoundException("Empresa não encontrada");

        CompletableFuture<List<InvoiceResponse>> invoicesFuture = CompletableFuture.supplyAsync(
                () -> invoiceService.findByCompanyId(company.getId()), EXECUTOR);

        List<InvoiceResponse> invoices = invoicesFuture.join();

        BigDecimal avgCompany = invoiceService.getAverageMonthlyInvoice(invoices);
        BigDecimal avgSector = getAverageOtherCompaniesInvoice(cnpj, company.getCnae());
        BigDecimal diff = avgCompany.subtract(avgSector);

        Long sectorCount = repository.countByCnae(company.getCnae());

        return CompanySectorResponse.builder()
                .averageSectorInvoice(avgSector)
                .sectorCompaniesAmount(sectorCount)
                .diffAverages(diff)
                .averageInvoice(avgCompany)
                .build();
    }

    public BigDecimal getAverageOtherCompaniesInvoice(String cnpj, String cnae) {
        if (cnae == null) return BigDecimal.ZERO;

        List<Object[]> basicInfos = repository.findBasicInfoByCnaeExcludingCnpj(cnae, cnpj);
        if (basicInfos.isEmpty()) return BigDecimal.ZERO;

        List<CompletableFuture<BigDecimal>> futures = basicInfos.stream()
                .map(info -> (UUID) info[0])
                .map(id -> CompletableFuture.supplyAsync(() -> {
                    List<InvoiceResponse> invoices = invoiceService.findByCompanyId(id);
                    return invoiceService.getAverageMonthlyInvoice(invoices);
                }, EXECUTOR))
                .toList();

        BigDecimal total = futures.stream()
                .map(CompletableFuture::join)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return futures.isEmpty() ? BigDecimal.ZERO :
                total.divide(BigDecimal.valueOf(futures.size()), 2, RoundingMode.HALF_UP);
    }
}

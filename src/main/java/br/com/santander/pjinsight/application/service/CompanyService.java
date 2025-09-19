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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CompanyService {

    private final CompanyRepository repository;
    private ProfileClassifierService profileClassifierService;
    private final ObjectMapper objectMapper;

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
        }

        repository.saveAll(companies);
    }

    public Page<CompanyResponse> findAllClassifiedCompanies(Integer page) {
        var pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.ASC));

        return repository.findAllClassified(pageable)
                .map(this::toResponse);
    }

    private boolean hasValidProfile(Company company) {
        return company.getProfile() != null && !company.getProfile().isBlank();
    }

    private CompanyResponse toResponse(Company company) {
        var response = new CompanyResponse();
        BeanUtils.copyProperties(company, response);
        return response;
    }

    public void classifyBatch(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = file.getOriginalFilename();

            if (fileName != null && fileName.endsWith(".csv")) {
                processCsv(inputStream);
            } else if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
                processExcel(inputStream);
            } else {
                throw new IllegalArgumentException("Formato de arquivo não suportado: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar arquivo", e);
        }
    }

    private void processCsv(InputStream inputStream) {
        try (CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .parse(new java.io.InputStreamReader(inputStream))) {

            Map<String, Integer> headerMap = parser.getHeaderMap();
            String cnpjHeader = headerMap.keySet().stream()
                    .filter(h -> h.equalsIgnoreCase("cnpj"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Coluna CNPJ não encontrada no CSV"));

            List<String> cnpjs = parser.getRecords().stream()
                    .map(record -> record.get(cnpjHeader))
                    .filter(s -> s != null && !s.isBlank())
                    .map(String::trim)
                    .toList();

            classifyCompanies(cnpjs);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar CSV", e);
        }
    }

    private void processExcel(InputStream inputStream) {
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            List<String> cnpjs = new ArrayList<>();

            int cnpjColumnIndex = 0;

            sheet.forEach(row -> {
                if (row.getRowNum() == 0) return;
                var cell = row.getCell(cnpjColumnIndex);
                if (cell != null) {
                    String cnpj = switch (cell.getCellType()) {
                        case STRING -> cell.getStringCellValue().trim();
                        case NUMERIC -> String.valueOf(new BigDecimal(cell.getNumericCellValue()).toBigInteger());
                        default -> "";
                    };
                    if (!cnpj.isBlank()) cnpjs.add(cnpj);
                }
            });

            classifyCompanies(cnpjs);

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar Excel", e);
        }
    }

    public CompanyResponse findByCnpj(String cnpj) {
        Company company = repository.findByCnpj(cnpj);
        CompanyResponse companyResponse = new CompanyResponse();
        BeanUtils.copyProperties(company,companyResponse);
        return companyResponse;
    }
}

package br.com.pjinsights.application.service;

import br.com.pjinsights.application.dto.request.InvoiceRequest;
import br.com.pjinsights.application.dto.response.InvoiceResponse;
import br.com.pjinsights.domain.entity.Invoice;
import br.com.pjinsights.infrastructure.repository.CompanyRepository;
import br.com.pjinsights.infrastructure.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final CompanyRepository companyRepository;
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(8);

    @Transactional
    public List<InvoiceResponse> save(List<InvoiceRequest> invoiceRequest, UUID companyId) {
        return invoiceRequest.stream().map(
                request -> {
                    Invoice invoice = new Invoice();
                    BeanUtils.copyProperties(request,invoice);
                    invoice.setCompanyId(companyId);
                    invoice = invoiceRepository.save(invoice);

                    InvoiceResponse invoiceResponse = new InvoiceResponse();
                    BeanUtils.copyProperties(invoice,invoiceResponse);

                    return invoiceResponse;
                }
        ).toList();
    }

    public List<InvoiceResponse> findByCompanyId(UUID companyId) {
        return invoiceRepository.findByCompanyId(companyId).stream().map(
                invoice -> {
                    InvoiceResponse invoiceResponse = new InvoiceResponse();
                    BeanUtils.copyProperties(invoice,invoiceResponse);

                    return invoiceResponse;
                }
        ).toList();
    }

    public BigDecimal getAverageMonthlyInvoice(List<InvoiceResponse> invoices) {
        if (invoices == null || invoices.isEmpty()) return BigDecimal.ZERO;

        BigDecimal total = invoices.stream()
                .map(InvoiceResponse::getInvoiceValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return total.divide(BigDecimal.valueOf(invoices.size()), 2, RoundingMode.HALF_UP);
    }
}

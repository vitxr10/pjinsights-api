package br.com.pjinsights.application.service;

import br.com.pjinsights.application.dto.request.InvoiceRequest;
import br.com.pjinsights.application.dto.response.InvoiceResponse;
import br.com.pjinsights.domain.entity.Invoice;
import br.com.pjinsights.infrastructure.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

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
}

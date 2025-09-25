package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.InvoiceRequest;
import br.com.santander.pjinsight.application.model.response.AddressResponse;
import br.com.santander.pjinsight.application.model.response.InvoiceResponse;
import br.com.santander.pjinsight.domain.entity.Address;
import br.com.santander.pjinsight.domain.entity.Invoice;
import br.com.santander.pjinsight.infrastructure.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

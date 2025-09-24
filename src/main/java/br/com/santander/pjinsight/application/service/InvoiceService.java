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
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    @Transactional
    public InvoiceResponse save(InvoiceRequest invoiceRequest, UUID companyId) {
        Invoice invoice = new Invoice();
        BeanUtils.copyProperties(invoiceRequest,invoice);
        invoice.setCompanyId(companyId);
        invoice = invoiceRepository.save(invoice);
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        BeanUtils.copyProperties(invoice,invoiceResponse);
        return invoiceResponse;
    }

    public InvoiceResponse findByCompanyId(UUID companyId) {
        InvoiceResponse invoiceResponse = new InvoiceResponse();
        Invoice invoice = invoiceRepository.findByCompanyId(companyId);
        BeanUtils.copyProperties(invoice,invoiceResponse);
        return invoiceResponse;
    }

}

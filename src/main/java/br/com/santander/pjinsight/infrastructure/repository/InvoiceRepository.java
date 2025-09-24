package br.com.santander.pjinsight.infrastructure.repository;

import br.com.santander.pjinsight.domain.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InvoiceRepository extends JpaRepository<Invoice, UUID> {
    Invoice findByCompanyId(UUID companyId);
}

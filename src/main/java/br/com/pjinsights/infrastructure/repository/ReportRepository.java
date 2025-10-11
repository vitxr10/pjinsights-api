package br.com.pjinsights.infrastructure.repository;

import br.com.pjinsights.domain.entity.Report;
import br.com.pjinsights.domain.enums.ReportTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    Report findByCnpjAndReportType(String cnpj, ReportTypeEnum reportType);
}

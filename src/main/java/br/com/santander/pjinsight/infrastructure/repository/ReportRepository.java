package br.com.santander.pjinsight.infrastructure.repository;

import br.com.santander.pjinsight.domain.entity.Report;
import br.com.santander.pjinsight.domain.enums.ReportTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReportRepository extends JpaRepository<Report, UUID> {
    Report findByCnpjAndReportType(String cnpj, ReportTypeEnum reportType);
}

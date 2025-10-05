package br.com.santander.pjinsight.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.santander.pjinsight.domain.entity.Company;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Company findByCnpj(String cnpj);

    List<Company> findByCnpjIn(List<String> cnpjs);

    List<Company> findByCnae(String cnae);

    @Query("SELECT c FROM Company c WHERE c.profile IS NOT NULL AND c.profile <> ''")
    Page<Company> findAllClassified(Pageable pageable);

    @Query("SELECT COUNT(c) FROM Company c WHERE c.cnae = :cnae")
    Long countByCnae(@Param("cnae") String cnae);
}

package br.com.pjinsights.infrastructure.repository;

import br.com.pjinsights.domain.entity.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @EntityGraph(attributePaths = {"profile", "cnae"})
    Company findByCnpj(String cnpj);

    @EntityGraph(attributePaths = {"profile", "cnae"})
    List<Company> findByCnpjIn(List<String> cnpjs);

    @Query("SELECT c FROM Company c WHERE c.cnae = :cnae")
    List<Company> findByCnae(@Param("cnae") String cnae);

    @Query("SELECT COUNT(c) FROM Company c WHERE c.cnae = :cnae")
    Long countByCnae(@Param("cnae") String cnae);

    @Query("SELECT c FROM Company c WHERE c.profile IS NOT NULL AND c.profile <> ''")
    Page<Company> findAllClassified(Pageable pageable);

    // 🔹 Busca apenas os campos necessários (evita SELECT *)
    @Query("SELECT c.id, c.cnae, c.cnpj FROM Company c WHERE c.cnae = :cnae AND c.cnpj <> :cnpj")
    List<Object[]> findBasicInfoByCnaeExcludingCnpj(@Param("cnae") String cnae, @Param("cnpj") String cnpj);
}

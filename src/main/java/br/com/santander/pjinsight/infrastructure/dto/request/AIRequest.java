package br.com.santander.pjinsight.infrastructure.dto.request;

import br.com.santander.pjinsight.domain.enums.ReportTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AIRequest {
    private String cnpj;
    private ReportTypeEnum reportType;
    private String prompt;
}

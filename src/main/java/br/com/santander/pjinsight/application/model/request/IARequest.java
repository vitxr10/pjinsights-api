package br.com.santander.pjinsight.application.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IARequest {
    String cnpj;
    String prompt;
}

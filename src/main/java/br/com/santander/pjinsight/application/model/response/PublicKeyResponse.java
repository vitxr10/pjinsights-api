package br.com.santander.pjinsight.application.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicKeyResponse {
    private String kty;
    private String alg;
    private String publicKeyPem;
}

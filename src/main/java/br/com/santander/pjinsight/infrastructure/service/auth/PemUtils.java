package br.com.santander.pjinsight.infrastructure.service.auth;

import java.security.PublicKey;
import java.util.Base64;

public class PemUtils {
    public static String toPemPublicKey(PublicKey publicKey) {
        String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        StringBuilder pem = new StringBuilder();
        pem.append("-----BEGIN PUBLIC KEY-----\n");

        int i = 0;
        while (i < encoded.length()) {
            int end = Math.min(i + 64, encoded.length());
            pem.append(encoded, i, end).append("\n");
            i = end;
        }
        pem.append("-----END PUBLIC KEY-----\n");
        return pem.toString();
    }
}

package br.com.santander.pjinsight.infrastructure.config.rsa;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Component
public class KeyPairConfig {
    private KeyPair keyPair;

    @PostConstruct
    public void init() throws NoSuchAlgorithmException {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(3072);
        this.keyPair = kpg.generateKeyPair();
    }

    public KeyPair getKeyPair() {
        return keyPair;
    }
}
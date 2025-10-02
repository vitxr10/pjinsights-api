package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.application.model.request.EncryptedRequest;
import br.com.santander.pjinsight.application.model.request.LoginRequest;
import br.com.santander.pjinsight.application.model.request.RegisterRequest;
import br.com.santander.pjinsight.application.model.response.LoginResponse;
import br.com.santander.pjinsight.application.model.response.PublicKeyResponse;
import br.com.santander.pjinsight.application.model.response.RegisterResponse;
import br.com.santander.pjinsight.domain.entity.User;
import br.com.santander.pjinsight.infrastructure.config.rsa.KeyPairConfig;
import br.com.santander.pjinsight.infrastructure.repository.UserRepository;
import br.com.santander.pjinsight.application.service.AuthorizationService;
import br.com.santander.pjinsight.infrastructure.service.auth.PemUtils;
import br.com.santander.pjinsight.infrastructure.service.auth.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.*;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenService tokenService;
    AuthorizationService authorizationService;
    private final KeyPairConfig keyPairConfig;


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid EncryptedRequest request) throws Exception {
        if (request.getEncrypted() == null) {
            return ResponseEntity.badRequest().build();
        }

        String decryptedJson = decryptPayload(request.getEncrypted());

        ObjectMapper mapper = new ObjectMapper();
        LoginRequest data = mapper.readValue(decryptedJson, LoginRequest.class);

        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponse(token));
    }


    @GetMapping("/public-key")
    public ResponseEntity<PublicKeyResponse> publicKey() {
        String pem = PemUtils.toPemPublicKey(keyPairConfig.getKeyPair().getPublic());

        PublicKeyResponse response = new PublicKeyResponse(
                "RSA",
                "RSA-OAEP-256",
                pem
        );

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(Duration.ofMinutes(60)).noTransform())
                .body(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid EncryptedRequest request) throws Exception {
        if (request.getEncrypted() == null) {
            return ResponseEntity.badRequest().build();
        }

        String decryptedJson = decryptPayload(request.getEncrypted());

        ObjectMapper mapper = new ObjectMapper();
        RegisterRequest data = mapper.readValue(decryptedJson, RegisterRequest.class);

        if (this.userRepository.findByLogin(data.getLogin()) != null) {
            return ResponseEntity.badRequest().build();
        }

        RegisterResponse registerResponse = authorizationService.insert(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }

    private String decryptPayload(String encryptedB64) throws Exception {
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedB64);

        PrivateKey privateKey = keyPairConfig.getKeyPair().getPrivate();
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

}

package br.com.santander.pjinsight.api.controller;

import br.com.santander.pjinsight.application.model.request.LoginRequest;
import br.com.santander.pjinsight.application.model.request.RegisterRequest;
import br.com.santander.pjinsight.application.model.response.LoginResponse;
import br.com.santander.pjinsight.application.model.response.RegisterResponse;
import br.com.santander.pjinsight.domain.entity.User;
import br.com.santander.pjinsight.infrastructure.repository.UserRepository;
import br.com.santander.pjinsight.application.service.AuthorizationService;
import br.com.santander.pjinsight.infrastructure.service.auth.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;
    private AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponse(data.getLogin(),token));

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest data) {
        if (this.userRepository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().build();
        RegisterResponse registerResponse = authorizationService.insert(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }



}

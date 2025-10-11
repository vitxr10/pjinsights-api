package br.com.pjinsights.api.controller;

import br.com.pjinsights.application.dto.request.LoginRequest;
import br.com.pjinsights.application.dto.request.RegisterRequest;
import br.com.pjinsights.application.dto.response.LoginResponse;
import br.com.pjinsights.application.dto.response.RegisterResponse;
import br.com.pjinsights.application.service.AuthorizationService;
import br.com.pjinsights.domain.entity.User;
import br.com.pjinsights.infrastructure.repository.UserRepository;
import br.com.pjinsights.infrastructure.service.auth.TokenService;
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
        if (this.userRepository.findByLogin(data.getLogin()) != null)
            return ResponseEntity.badRequest().build();

        RegisterResponse registerResponse = authorizationService.insert(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);
    }
}

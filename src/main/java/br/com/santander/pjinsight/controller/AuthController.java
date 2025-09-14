package br.com.santander.pjinsight.controller;

import br.com.santander.pjinsight.dto.req.LoginRequestDTO;
import br.com.santander.pjinsight.dto.req.RegisterRequest;
import br.com.santander.pjinsight.dto.res.LoginResponseDTO;
import br.com.santander.pjinsight.dto.res.RegisterResponse;
import br.com.santander.pjinsight.model.User;
import br.com.santander.pjinsight.repository.UserRepository;
import br.com.santander.pjinsight.service.AuthorizationService;
import br.com.santander.pjinsight.service.TokenService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private TokenService tokenService;
    AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));

    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest data) {
        if (this.userRepository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().build();
        RegisterResponse registerResponse = authorizationService.insert(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerResponse);

    }



}

package br.com.santander.pjinsight.controller;

import br.com.santander.pjinsight.dto.req.AuthenticationDTO;
import br.com.santander.pjinsight.dto.req.RegisterDTO;
import br.com.santander.pjinsight.model.User;
import br.com.santander.pjinsight.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getLogin(), data.getPassword());
        var auth = authenticationManager.authenticate(usernamePassword);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByLogin(data.getLogin()) != null) return ResponseEntity.badRequest().build();
        String encryptedPassword = passwordEncoder.encode(data.getPassword());
        User newUser = new User(data.getLogin(), encryptedPassword, data.getRole());
        this.userRepository.save(newUser);
        return ResponseEntity.ok().build();

    }



}

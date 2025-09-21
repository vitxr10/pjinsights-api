package br.com.santander.pjinsight.application.service;

import br.com.santander.pjinsight.application.model.request.RegisterRequest;
import br.com.santander.pjinsight.application.model.response.RegisterResponse;
import br.com.santander.pjinsight.domain.entity.User;
import br.com.santander.pjinsight.infrastructure.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public RegisterResponse insert(RegisterRequest registerRequest){
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());

        User newUser = new User(
                registerRequest.getLogin(),
                encryptedPassword,
                registerRequest.getRole()
        );

        newUser = this.userRepository.save(newUser);

        // Conversão direta com Jackson
        return objectMapper.convertValue(newUser, RegisterResponse.class);
    }
}

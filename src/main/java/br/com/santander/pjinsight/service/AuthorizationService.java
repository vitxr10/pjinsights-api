package br.com.santander.pjinsight.service;


import br.com.santander.pjinsight.dto.req.RegisterRequest;
import br.com.santander.pjinsight.dto.res.RegisterResponse;
import br.com.santander.pjinsight.model.User;
import br.com.santander.pjinsight.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static br.com.santander.pjinsight.mapper.ObjectMapper.*;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public RegisterResponse insert(RegisterRequest registerRequest){
        String encryptedPassword = passwordEncoder.encode(registerRequest.getPassword());
        User newUser = new User(registerRequest.getLogin(), encryptedPassword, registerRequest.getRole());
        newUser = this.userRepository.save(newUser);
        return parseObject(newUser,RegisterResponse.class);
    }
}

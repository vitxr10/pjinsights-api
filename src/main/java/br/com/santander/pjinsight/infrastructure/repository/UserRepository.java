package br.com.santander.pjinsight.infrastructure.repository;

import br.com.santander.pjinsight.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<User, String> {

    UserDetails findByLogin(String login);


}

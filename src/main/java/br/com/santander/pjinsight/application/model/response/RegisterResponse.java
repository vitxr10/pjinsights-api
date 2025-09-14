package br.com.santander.pjinsight.application.model.response;

import br.com.santander.pjinsight.domain.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterResponse {
    private String id;
    private String login;
    private String password;
    private UserRoleEnum role;
}

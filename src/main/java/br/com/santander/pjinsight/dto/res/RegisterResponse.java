package br.com.santander.pjinsight.dto.res;

import br.com.santander.pjinsight.model.enums.UserRoleEnum;
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

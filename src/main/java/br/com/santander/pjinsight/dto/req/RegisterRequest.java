package br.com.santander.pjinsight.dto.req;

import br.com.santander.pjinsight.model.enums.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    private String login;
    private String password;
    private UserRoleEnum role;

}

package pl.com.tt.intern.soccer.model.account;

import lombok.Data;

@Data
public class PasswordChanger {
    private String password;
    private String confirmPassword;
}

package pl.com.tt.intern.soccer.model.account;

import lombok.Data;

@Data
public class ChangePassword {
    private String password;
    private String confirmPassword;
}

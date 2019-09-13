package pl.com.tt.intern.soccer.security.model;

import lombok.Data;

@Data
public class LogIn {
    private String usernameOrEmail;
    private String password;
}

package pl.com.tt.intern.soccer.security;

import lombok.Data;

@Data
public class LogIn {
    private String usernameOrEmail;
    private String password;
}

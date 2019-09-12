package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;
import pl.com.tt.intern.soccer.annotation.Username;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SignUpRequest {

    @Username
    private String username;

    @Email
    @NotBlank
    private String email;

    @Password
    @NotBlank
    private String password;

    @NotBlank
    private String confirmPassword;

    @Size(min = 3, max = 20)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 20)
    @NotBlank
    private String lastName;

}

package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static pl.com.tt.intern.soccer.util.Messages.VALID_USERNAME;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 5, max = 20)
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = VALID_USERNAME)
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

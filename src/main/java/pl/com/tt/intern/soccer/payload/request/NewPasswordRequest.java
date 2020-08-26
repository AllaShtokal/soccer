package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;

import javax.validation.constraints.NotBlank;

@Data
public class NewPasswordRequest {

    @Password
    @NotBlank
    private String password;

    @NotBlank
    private String passwordConfirmation;
}

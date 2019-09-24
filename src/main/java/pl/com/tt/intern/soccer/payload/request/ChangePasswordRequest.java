package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;
import pl.com.tt.intern.soccer.annotation.Password;

import javax.validation.constraints.NotBlank;

@Data
public class ChangePasswordRequest {

    @NotBlank
    private String oldPassword;

    @Password
    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordConfirmation;
}

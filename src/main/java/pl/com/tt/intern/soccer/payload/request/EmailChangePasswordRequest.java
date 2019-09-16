package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class EmailChangePasswordRequest {

    @Email
    @NotBlank
    private String email;
}

package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangeAccountDataRequest {

    @Size(min = 3, max = 20)
    @NotBlank
    private String firstName;

    @Size(min = 3, max = 20)
    @NotBlank
    private String lastName;

    @Size(max = 12)
    private String phone;

    @Size(max = 30)
    private String skype;
}

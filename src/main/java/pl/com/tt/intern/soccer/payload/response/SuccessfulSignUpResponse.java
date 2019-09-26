package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.User;

@Data
public class SuccessfulSignUpResponse {

    private String message;
    private String firstName;
    private String lastName;
    private String email;

    public SuccessfulSignUpResponse(String message, User user) {
        this.message = message;
        this.firstName = user.getUserInfo().getFirstName();
        this.lastName = user.getUserInfo().getLastName();
        this.email = user.getEmail();
    }

}

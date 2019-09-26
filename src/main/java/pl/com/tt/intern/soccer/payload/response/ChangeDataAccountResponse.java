package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.User;

@Data
public class ChangeDataAccountResponse {

    private String firstName;
    private String lastName;
    private String phone;
    private String skype;
    private String email;

    public ChangeDataAccountResponse(User user) {
        this.firstName = user.getUserInfo().getFirstName();
        this.lastName = user.getUserInfo().getLastName();
        this.phone = user.getUserInfo().getPhone();
        this.skype = user.getUserInfo().getSkype();
        this.email = user.getEmail();
    }

}

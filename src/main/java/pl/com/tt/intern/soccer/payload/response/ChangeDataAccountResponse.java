package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.UserInfo;

@Data
public class ChangeDataAccountResponse {

    private String firstName;
    private String lastName;
    private String phone;
    private String skype;
    private String email;

    public ChangeDataAccountResponse(UserInfo userInfo) {
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
        this.phone = userInfo.getPhone();
        this.skype = userInfo.getSkype();
        this.email = userInfo.getUser().getEmail();
    }

}

package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.security.UserPrincipal;

@Data
public class CurrentUserInfoResponse {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String skype;

    public CurrentUserInfoResponse(UserPrincipal userPrincipal) {
        this.username = userPrincipal.getUsername();
        this.email = userPrincipal.getEmail();
        this.firstName = userPrincipal.getUserInfo().getFirstName();
        this.lastName = userPrincipal.getUserInfo().getLastName();
        this.phone = userPrincipal.getUserInfo().getPhone();
        this.skype = userPrincipal.getUserInfo().getSkype();
    }
}

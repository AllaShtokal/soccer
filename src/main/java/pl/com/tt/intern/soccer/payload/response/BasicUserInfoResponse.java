package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.security.UserPrincipal;

@Data
public class BasicUserInfoResponse {

    private String username;
    private String email;
    private String won;
    private String lost;
    private String ranking;


    public BasicUserInfoResponse() {
    }

    public BasicUserInfoResponse(UserPrincipal userPrincipal) {
        this.username = userPrincipal.getUsername();
        this.email = userPrincipal.getEmail();
        this.won = userPrincipal.getUserInfo().getWon().toString();
        this.lost = userPrincipal.getUserInfo().getLost().toString();

    }

    public BasicUserInfoResponse(String toString) {
    }


}

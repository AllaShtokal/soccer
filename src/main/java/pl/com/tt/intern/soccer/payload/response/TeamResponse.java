package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.security.UserPrincipal;

import java.util.Set;

@Data
public class TeamResponse {

    private  Long team_id;
    private String name;
    private Set<BasicUserInfoResponse> users;
}

package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

import java.util.Set;

@Data
public class TeamResponse {

    private  Long team_id;
    private String name;
    private Set<BasicUserInfoResponse> users;
}

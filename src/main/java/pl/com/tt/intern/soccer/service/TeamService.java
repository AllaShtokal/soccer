package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;

import java.util.Set;

public interface TeamService {
    Long getTeamIdByTeamName(String teamName);
    void save (Team team);

    Set<BasicUserInfoResponse> getUsersByTeamName(String teamName);

}

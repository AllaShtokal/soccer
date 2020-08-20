package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;

import java.util.Set;

public interface TeamService {
    public Long getTeamIdByTeamName(String teamName);

    public Set<BasicUserInfoResponse> getUsersByTeamName(String teamName);
}

package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;

import java.util.List;
import java.util.Set;

public interface GameService {

    List<TeamResponse> getSetOfWinnersByGameID(Long id);
    List<ButtleResponse> generateListOfButtlesFromListOfTeams(Set<Team> teamList);
}

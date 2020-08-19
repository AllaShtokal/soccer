package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;

import java.util.List;
import java.util.Set;

public interface ButtleService {

    Set<String> getSetOfNamesOfTeamWinners(Set<Buttle> buttles);
    void setActiveTeamsByListOfButtles(Set<Team> teams,Set<Buttle> buttles);
    List<ButtleResponse> getAllButtlesByGameID(Long game_id);

}

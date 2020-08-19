package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;

import java.util.List;
import java.util.Set;

public interface ButtleService {

    Set<String> getSetOfNamesOfTeamWinners(Set<Buttle> buttles);
    void setActiveTeamsByListOfButtles(Set<Team> teams,Set<Buttle> buttles);
    List<ButtleResponse> getAllButtlesByGameID(Long gameId) throws NotFoundException;
     TeamResponse getTeamWinner(ButtleResponse buttle);

}

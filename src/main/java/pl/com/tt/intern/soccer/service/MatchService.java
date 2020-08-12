package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;

import java.util.Set;

public interface MatchService {

    MatchResponseRequest play(Long reservation_id);
    //MatchResponseRequest play(Long reservation_id);
    Boolean saveResults(MatchResponseRequest matchResponseRequest);
    Game getActiveGameFromMatch(Match match);
    Set<Team> getActiveTeamsFromMatch(Match match);


}

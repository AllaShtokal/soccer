package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.MatchFullResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;

import java.util.List;
import java.util.Set;

public interface MatchService {

    MatchResponseRequest play(Long reservation_id);
    List<MatchFullResponse> findAllByReservationId(Long reservation_id);
    Boolean saveResults(MatchResponseRequest matchResponseRequest);
    int getNumberOfActiveTeamsByMatchId(Match activeMatch);
    Set<Team> getActiveTeamsFromMatch(Match match);
    Match getActiveMatch(Long reservation_id);


}

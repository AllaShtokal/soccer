package pl.com.tt.intern.soccer.service;


import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.request.TeamRequest;
import pl.com.tt.intern.soccer.payload.response.MatchFullResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.payload.response.MatchResultsResponse;

import java.util.List;
import java.util.Set;

public interface MatchService {

    MatchResponseRequest play(Long reservationId, List<TeamRequest> teamRequests) throws NotFoundException;
    List<MatchFullResponse> findAllByReservationId(Long reservationId) throws NotFoundException;
    MatchResultsResponse getMatchResult(Long matchId) throws NotFoundException;
    Boolean saveResults(MatchResponseRequest matchResponseRequest) throws NotFoundException;
    int getNumberOfActiveTeamsByMatchId(Match activeMatch);
    Set<Team> getActiveTeamsFromMatch(Match match);
    Match getActiveMatch(Long reservationId);


}

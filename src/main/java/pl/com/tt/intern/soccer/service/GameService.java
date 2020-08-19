package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.GameResponse;

import java.util.List;
import java.util.Set;

public interface GameService {


    Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teamList) throws Exception;
    Game getActiveGameFromMatch(Match match);
    List<GameResponse> getAllGamesFromMatch(Long matchId) throws NotFoundException;
    GameResponse getlastGameInMatch(Long matchId) throws NotFoundException;
}

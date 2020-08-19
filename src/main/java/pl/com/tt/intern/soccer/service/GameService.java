package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.GameResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;

import java.util.List;
import java.util.Set;

public interface GameService {


    Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teamList);
    Game getActiveGameFromMatch(Match match);
    List<GameResponse> getAllGamesFromMatch(Long match_id);
}

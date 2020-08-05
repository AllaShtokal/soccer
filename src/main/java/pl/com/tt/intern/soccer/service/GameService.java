package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;

import java.util.Set;

public interface GameService {

    Set<Team> getSetOfWinnersByGameID(Long id);
    Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> tempTeamList);
}

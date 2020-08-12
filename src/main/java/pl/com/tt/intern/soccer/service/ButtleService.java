package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;

import java.util.Set;

public interface ButtleService {

    Set<String> getSetOfNamesOfTeamWinners(Set<Buttle> buttles);
    void setActiveTeams(Set<Team> teams,Set<Buttle> buttles);

}

package pl.com.tt.intern.soccer.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.MatchService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ButtleServiceImpl implements ButtleService {

    @Override
    public Set<String> getSetOfNamesOfTeamWinners(Set<Buttle> buttles) {
        Set<String> activeTeamsNames = new HashSet<>();
        for (Buttle b : buttles) {
            activeTeamsNames.add(getTeamNameWinner(b));
        }

        return activeTeamsNames;
    }

    private String getTeamNameWinner(Buttle buttle) {
        if (buttle.getScoreTeam1() > buttle.getScoreTeam2())
            return buttle.getTeamName1();
            //if first and second is the same?????! for now it also returns second team
        else return buttle.getTeamName2();
    }

    @Override
    public void setActiveTeams(Set<Team> teams, Set<Buttle> buttles) {

        Set<String> activeTeamsNames = getSetOfNamesOfTeamWinners(buttles);
        for (Team t : teams) {
            if (!activeTeamsNames.contains(t.getName())) {
                t.setActive(false);
            }
        }

    }


}

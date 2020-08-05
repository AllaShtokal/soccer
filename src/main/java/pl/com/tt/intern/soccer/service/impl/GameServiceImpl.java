package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.repository.TeamRepository;
import pl.com.tt.intern.soccer.service.GameService;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final ButtleServiceImpl buttleService;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    //works only if set of teams is paired
    @Override
    public Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teamList) {

        Set<Buttle> buttles = new HashSet<>();
        for (Iterator<Team> it = teamList.iterator(); it.hasNext(); ) {
            Team team = it.next();
            Team team2 = it.next();
            buttles.add(new Buttle(team.getName(), team2.getName()));
        }
        return buttles;
    }

    //return Set of winners

    @Override
    public Set<Team> getSetOfWinnersByGameID(Long id) {
        Set<Team> winners = new HashSet<>();
        Set<Buttle> batlles = gameRepository.findById(id).get().getListOfButtles();
        for (Buttle battle : batlles) {
            String nameOfTheTeam = buttleService.getTeamWinner(battle);
            winners.add(teamRepository.findByName(nameOfTheTeam).get());
        }

        return winners;
    }
}

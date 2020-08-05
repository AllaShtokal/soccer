package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.service.MatchService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final GameServiceImpl gameService;
    private final MatchRepository matchRepository;

    @Override
    public Game createGameByMatchId(Long id) {
       Game game = new Game();
       game.setListOfButtles(gameService.generateListOfButtlesFromListOfTeams(getCurrentActiveTeams(id)));

        return null;
    }

    private Set<Team> getCurrentActiveTeams(Long id){
        Set<Team> fullTeamList = matchRepository.findById(id).get().getFullTeamList();
        Set<Team> currentTeamList = new HashSet<>();
        for (Team team: fullTeamList)
        {
            if(team.getActive().equals(true))
                currentTeamList.add(team);
        }
        return currentTeamList;



    }




}

package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.GameResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.repository.TeamRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.GameService;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {


    private final MatchRepository matchRepository;
    private final ButtleService buttleService;

    //works only if set of teams is paired
    @Override
    public Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teams) {

        Set<Team> activeTeams = new HashSet<>();
        for(Team team: teams){
            if(team.getActive())
                activeTeams.add(team);
        }

        Set<Buttle> buttles = new HashSet<>();
        for (Iterator<Team> it = activeTeams.iterator(); it.hasNext(); ) {
            Buttle buttle = new Buttle();
            buttle.setTeamName1(it.next().getName());
            Team team2 = it.next();
            buttle.setTeamName2(team2.getName());
            buttles.add(buttle);
        }
        return buttles;

    }

    @Override
    public Game getActiveGameFromMatch(Match match) {
        Set<Game> games = match.getGames();
        Game activeGame = new Game();
        for (Game game : games) {
            if (game.getIsActive())
            { activeGame = game;
                return activeGame;}
        }
        return null;
    }

    @Override
    public List<GameResponse> getAllGamesFromMatch(Long match_id) {
        Match matchById  = matchRepository.findById(match_id).get();
        Set<Game> games  = matchById.getGames();
        List<GameResponse>  gameResponses= new ArrayList<>();

        for (Game g: games)
        {
            GameResponse gameResponse = new GameResponse();
            gameResponse.setGameId(g.getId());
            gameResponse.setButtles(buttleService.getAllButtlesByGameID(g.getId()));
            gameResponses.add(gameResponse);
        }
        return gameResponses;


    }

}

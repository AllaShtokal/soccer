package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.GameResponse;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.GameService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final MatchRepository matchRepository;
    private final ButtleService buttleService;

    //works only if set of teams is paired
    @Override
    public Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teams, Game game) throws RuntimeException {


        Set<Team> activeTeams = new HashSet<>();
        for (Team team : teams) {
            if (Boolean.TRUE.equals(team.getActive()))
                activeTeams.add(team);
        }
        Set<Buttle> buttles = new HashSet<>();
        if (activeTeams.size() % 2 != 0) {
            log.debug("number of teams is NOT paired ");
            throw new RuntimeException("number of teams is NOT paired ");
        }

        for (Iterator<Team> it = activeTeams.iterator(); it.hasNext(); ) {
            Buttle buttle = new Buttle();
            buttle.setGame(game);
            buttle.setTeamName1(it.next().getName());
            Team team2 = it.next();
            buttle.setTeamName2(team2.getName());
            buttleService.save(buttle);
            buttles.add(buttle);
        }
        return buttles;

    }

    @Override
    public Game getActiveGameFromMatch(Match match) {
        Set<Game> games = match.getGames();
        Game activeGame;
        for (Game game : games) {
            if (Boolean.TRUE.equals(game.getIsActive())) {
                activeGame = game;
                return activeGame;
            }
        }
        return null;
    }

    @Override
    public List<GameResponse> getAllGamesFromMatch(Long match_id) throws NotFoundException {
        Match matchById = matchRepository.findById(match_id).orElseThrow(NotFoundException::new);
        Set<Game> games = matchById.getGames();
        List<GameResponse> gameResponses = new ArrayList<>();

        for (Game g : games) {
            GameResponse gameResponse = new GameResponse();
            gameResponse.setGameId(g.getId());
            gameResponse.setIsActive(g.getIsActive());
            gameResponse.setButtles(buttleService.getAllButtlesByGameID(g.getId()));
            gameResponses.add(gameResponse);
        }
        return gameResponses;


    }

    @Override
    public GameResponse getlastGameInMatch(Long matchId) throws NotFoundException {
        Match matchById = matchRepository.findById(matchId).orElseThrow(NotFoundException::new);
        Set<Game> games = matchById.getGames();
        Long tmp = 0L;
        for (Game g : games) {

            if (g.getId() > tmp)
                tmp = g.getId();
        }
        GameResponse gameResponse = new GameResponse();
        gameResponse.setGameId(tmp);
        gameResponse.setButtles(buttleService.getAllButtlesByGameID(tmp));
        return gameResponse;
    }

    @Override
    public void save(Game game) {
        gameRepository.save(game);

    }

}

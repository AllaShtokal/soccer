package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundReservationException;
import pl.com.tt.intern.soccer.model.*;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.MatchService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final GameServiceImpl gameService;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;


    @Transactional
    @Override
    public MatchResponse create(Long reservation_id) {
        Reservation reservation = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);
        Match m = creteMatch(reservation_id);
        reservation.addMatch(m);
        Reservation reservationAfter = reservationRepository.save(reservation);

        MatchResponse matchResponse = new MatchResponse();
        matchResponse.setMatchId(m.getId());

        //get buttles from acctive game
        Set<Match> matches = reservationAfter.getMatches();
        Match activeMatch = new Match();
        for (Match match : matches) {
            if (match.getIsActive())
                activeMatch = match;
            break;
        }

        Set<Game> games = activeMatch.getGames();
        Game activeGame = new Game();
        for (Game game : games) {
            if (game.getIsActive())
                activeGame = game;
            break;
        }

        matchResponse.setActiveButtles(mapToResponse(activeGame.getButtles()));

        return null;
    }


    private Match creteMatch(Long reservation_id) {
        Match match = new Match();
        match.setDateFrom(LocalDateTime.now());
        match.setIsActive(true);

        //setTeams
        Set<Team> teams = generateTeams(reservation_id);
        for (Team team : teams) {
            match.addTeam(team);
        }

        //setGames(add one new)
        Game game = createGame(teams);
        match.addGame(game);
        return match;
    }

    private Game createGame(Set<Team> teams) {
        Game game = new Game();
        game.setIsActive(true);
        game.setButtles(gameService.generateListOfButtlesFromListOfTeams(teams));
        return game;
    }

    private Set<Team> generateTeams(Long reservation_id) {

        List<User> users = new ArrayList<>();
        Set<UserReservationEvent> userReservationEvents = reservationRepository.findById(reservation_id).get().getUserReservationEvents();
        for (UserReservationEvent userReservationEvent : userReservationEvents) {
            users.add(userReservationEvent.getUser());
        }

        Set<Team> teams = new HashSet<>();
        for (User user : users) {
            int i = 0;
            Team t = new Team();
            t.setActive(true);
            t.setName("Team Name " + i);
            i++;
            t.setUsers((Set<User>) user);

            teams.add(t);
        }
        return teams;

    }


    private List<ButtleResponse> mapToResponse(Set<Buttle> teams) {
        return teams.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    private ButtleResponse mapToResponse(Buttle buttle) {
        return modelMapper.map(buttle, ButtleResponse.class);
    }

}

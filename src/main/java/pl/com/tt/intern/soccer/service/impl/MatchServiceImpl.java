package pl.com.tt.intern.soccer.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundReservationException;
import pl.com.tt.intern.soccer.exception.service.RandomString;
import pl.com.tt.intern.soccer.model.*;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.MatchService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final GameServiceImpl gameService;
    private final ButtleService buttleService;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;
    private final RandomString randomString;
    private final MatchRepository matchRepository;


    @Transactional
    @Override
    public MatchResponseRequest play(Long reservation_id) {
        Reservation reservation = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);

        if (getActiveMatch(reservation_id) == null) {
            //then create new match and all stuff
            Match m = creteMatch();
            setGamesToMatch(setTeamsToMatch(reservation_id, m), m);
            reservation.addMatch(m);
            reservationRepository.save(reservation);
        } else
            {

            Match activeMatch = getActiveMatch(reservation_id);
            if(getActiveGameFromMatch(activeMatch)==null)
            {setGamesToMatch(setTeamsToMatch(reservation_id, activeMatch), activeMatch);
           reservationRepository.save(reservation);}

        }


        //create response
        Reservation reservationAfter = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);
        Match activeMatch1 = getActiveMatch(reservationAfter.getId());
        MatchResponseRequest matchResponse = new MatchResponseRequest();
        matchResponse.setMatchId(activeMatch1.getId());
        matchResponse.setActiveButtles(mapToResponse(getActiveGameFromMatch(activeMatch1).getButtles()));

        return matchResponse;
    }

    //getAllMatches

    public Set<Match> findAll(Long reservation_id) {
        Reservation reservation = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);
        return reservation.getMatches();
    }

    private Match getActiveMatch(Long reservation_id) {
        Set<Match> allMatches = findAll(reservation_id);
        for (Match m : allMatches) {
            if (m.getIsActive())
                return m;

        }
        return null;
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
    public Set<Team> getActiveTeamsFromMatch(Match match) {
        Set<Team> teams = match.getTeams();
        Set<Team> activeTeams = new HashSet<>();
        for (Team t : teams) {
            if (t.getActive()) {
                activeTeams.add(t);
            }
        }
        return activeTeams;

    }

    @Transactional
    @Override
    public Boolean saveResults(MatchResponseRequest matchResponseRequest) {

        Match matchById = matchRepository.findById(matchResponseRequest.getMatchId()).get();
        Game game = getActiveGameFromMatch(matchById);
        game.setIsActive(false);
        //set not active teams
        buttleService.setActiveTeams(matchById.getTeams(),
                updateButtlesInfo(matchResponseRequest.getActiveButtles(), game.getButtles()));
        boolean result = false;
        //chek number of active teams if it is more then 1 bool set as true
        if (getNumberOfActiveTeams(matchById) > 1) {
            result = true;
        }

        if(!result)
        {matchById.setIsActive(false);
        matchById.setDateTo(LocalDateTime.now());
        }
        matchRepository.save(matchById);
        return result;
    }

    public int getNumberOfActiveTeams(Match activeMatch) {
        int num = 0;
        num = getActiveTeamsFromMatch(activeMatch).size();
        return num;
    }

    private Set<Buttle> updateButtlesInfo(List<ButtleResponse> requestButtles, Set<Buttle> buttles) {

        for (int i = 0; i < requestButtles.size(); i++)
            for (Buttle b : buttles) {
                if (requestButtles.get(i).getId().equals(b.getId())) {
                    b.setScoreTeam1(requestButtles.get(i).getScoreTeam1());
                    b.setScoreTeam2(requestButtles.get(i).getScoreTeam2());
                    break;
                }

            }
        return buttles;

    }


    private Match creteMatch() {
        Match match = new Match();
        match.setDateFrom(LocalDateTime.now());
        match.setIsActive(true);
        return match;
    }

    private void setGamesToMatch(Set<Team> teams, Match match) {
        //if there is no active game
        Game game = createGame(teams);
        match.addGame(game);
    }

    private Set<Team> setTeamsToMatch(Long reservation_id, Match match) {
        //if match has no teams then generate teams and return them
        if(match.getTeams().size()==0)
        {Set<Team> teams = generateTeams(reservation_id);
        for (Team team : teams) {
            match.addTeam(team);
        }
        return teams;}
        else return getActiveTeamsFromMatch(match);


    }


    private Game createGame(Set<Team> teams) {
        Game game = new Game();
        game.setIsActive(true);
        Set<Buttle> buttles = gameService.generateListOfButtlesFromListOfTeams(teams);
        for (Buttle b : buttles) {
            game.addButtle(b);
        }
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
            Team t = new Team();
            t.setActive(true);
            t.setName("Team:" + randomString.getAlphaNumericString(6));
            t.addUser(user);
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

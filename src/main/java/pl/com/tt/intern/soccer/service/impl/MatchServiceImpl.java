package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundReservationException;
import pl.com.tt.intern.soccer.exception.service.RandomString;
import pl.com.tt.intern.soccer.model.*;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.MatchFullResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.payload.response.MatchResultsResponse;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.GameService;
import pl.com.tt.intern.soccer.service.MatchService;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final GameService gameService;
    private final ButtleService buttleService;
    private final ModelMapper modelMapper;
    private final ReservationRepository reservationRepository;
    private final RandomString randomString;
    private final MatchRepository matchRepository;
    private final ReservationService reservationService;


    @Transactional
    @Override
    public MatchResponseRequest play(Long reservation_id) throws Exception {
        Reservation reservation = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);

        if (getActiveMatch(reservation_id) == null) {
            Match m = creteMatch();
            setGamesToMatch(setTeamsToMatch(reservation_id, m), m);
            reservation.addMatch(m);
            reservationRepository.save(reservation);
        } else {

            Match activeMatch = getActiveMatch(reservation_id);
            if (gameService.getActiveGameFromMatch(activeMatch) == null) {
                Set<Team> teams = setTeamsToMatch(reservation_id, activeMatch);
                setGamesToMatch(teams, activeMatch);
                reservationRepository.save(reservation);
            }

        }


        //create response
        Reservation reservationAfter = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);
        Match activeMatch1 = getActiveMatch(reservationAfter.getId());
        MatchResponseRequest matchResponse = new MatchResponseRequest();
        matchResponse.setMatchId(activeMatch1.getId());
        matchResponse.setActiveButtles(mapToResponse(gameService.getActiveGameFromMatch(activeMatch1).getButtles()));

        return matchResponse;
    }

    @Override
    public List<MatchFullResponse> findAllByReservationId(Long reservation_id) {
        Set<Match> matches = getAllMatches(reservation_id);
        List<MatchFullResponse> matchFullResponses = new ArrayList<>();
        for(Match m: matches)
        {
            MatchFullResponse matchFullResponse = new MatchFullResponse();
            matchFullResponse.setMatchId(m.getId());
            matchFullResponse.setGameResponses(gameService.getAllGamesFromMatch(m.getId()));
            matchFullResponses.add(matchFullResponse);
        }

        return matchFullResponses;
    }

    @Override
    public MatchResultsResponse getMatchResult(Long match_id) {
        Match matchById = matchRepository.findById(match_id).get();
        MatchResultsResponse matchResultsResponse = new MatchResultsResponse();
        matchResultsResponse.setMatchId(matchById.getId());
        matchResultsResponse.setGameResponses(gameService.getAllGamesFromMatch(matchById.getId()));
        matchResultsResponse.setTeamWinner(reservationService.getWinnerTeamByMatch(match_id));
        return matchResultsResponse;

    }


    public Set<Match> getAllMatches(Long reservation_id) {
        Reservation reservation = reservationRepository.findById(reservation_id).orElseThrow(NotFoundReservationException::new);
        return reservation.getMatches();
    }






    @Override
    public Match getActiveMatch(Long reservation_id) {
        Set<Match> allMatches = getAllMatches(reservation_id);
        for (Match m : allMatches) {
            if (m.getIsActive())
                return m;
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
        Game game = gameService.getActiveGameFromMatch(matchById);
        game.setIsActive(false);
        //set not active teams
        buttleService.setActiveTeamsByListOfButtles(matchById.getTeams(),
                updateButtlesInfo(matchResponseRequest.getActiveButtles(), game.getButtles()));
        boolean result = false;
        //chek number of active teams if it is more then 1 bool set as true
        if (getNumberOfActiveTeamsByMatchId(matchById) > 1) {
            result = true;
        }

        if (!result) {
            matchById.setIsActive(false);
            LocalDateTime matchEndTime = LocalDateTime.now();
            matchById.setDateTo(matchEndTime);

        }
        matchRepository.save(matchById);
        return result;
    }

    @Override
    public int getNumberOfActiveTeamsByMatchId(Match activeMatch) {
        return getActiveTeamsFromMatch(activeMatch).size();
    }

    private Set<Buttle> updateButtlesInfo(List<ButtleResponse> requestButtles, Set<Buttle> buttles) {

        for (ButtleResponse requestButtle : requestButtles)
            for (Buttle b : buttles) {
                if (requestButtle.getId().equals(b.getId())) {
                    b.setScoreTeam1(requestButtle.getScoreTeam1());
                    b.setScoreTeam2(requestButtle.getScoreTeam2());
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

    private void setGamesToMatch(Set<Team> teams, Match match) throws Exception {
        //if there is no active game
        Game game = createGame(teams);
        match.addGame(game);
    }

    private Set<Team> setTeamsToMatch(Long reservation_id, Match match) {
        //if match has no teams then generate teams and return them
        if (match.getTeams().size() == 0) {
            Set<Team> teams = generateTeams(reservation_id);
            for (Team team : teams) {
                match.addTeam(team);
            }
            return teams;
        } else return getActiveTeamsFromMatch(match);


    }


    private Game createGame(Set<Team> teams) throws Exception {
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

package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.NotFoundReservationException;
import pl.com.tt.intern.soccer.exception.service.RandomString;
import pl.com.tt.intern.soccer.model.*;
import pl.com.tt.intern.soccer.payload.request.TeamRequest;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.MatchFullResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.payload.response.MatchResultsResponse;
import pl.com.tt.intern.soccer.repository.MatchRepository;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.service.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final UserRepository userRepository;


    @Transactional
    @Override
    public MatchResponseRequest play(Long reservationId, List<TeamRequest> teamRequests) throws Exception {

        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundReservationException::new);

        if (getActiveMatch(reservationId) == null) {
            Match m = creteMatch();
            if (teamRequests == null) {
                setGamesToMatch(setTeamsToMatch(reservationId, m), m);
            } else {

                setGamesToMatch(getTeamsFromTeamRequest(teamRequests, m), m);
            }
            reservation.addMatch(m);
            reservationRepository.save(reservation);
        } else {

            Match activeMatch = getActiveMatch(reservationId);
            if (gameService.getActiveGameFromMatch(activeMatch) == null) {
                Set<Team> teams = setTeamsToMatch(reservationId, activeMatch);
                setGamesToMatch(teams, activeMatch);
                reservationRepository.save(reservation);
            }

        }
        //create response
        Reservation reservationAfter = reservationRepository.findById(reservationId).orElseThrow(NotFoundReservationException::new);
        Match activeMatch1 = getActiveMatch(reservationAfter.getId());
        MatchResponseRequest matchResponse = new MatchResponseRequest();
        matchResponse.setMatchId(activeMatch1.getId());
        matchResponse.setActiveButtles(mapToResponse(gameService.getActiveGameFromMatch(activeMatch1).getButtles()));

        return matchResponse;
    }

    private Set<Team> getTeamsFromTeamRequest(List<TeamRequest> teamRequests, Match match) throws NotFoundException {
        Set<Team> teams = new HashSet<>();
        List<User> users = userRepository.findAll();

        for (TeamRequest t : teamRequests) {
            Team team = new Team();
            team.setName(t.getTeamName());
            team.setActive(true);
            Set<String> usernames = t.getUsernames();

            for (String n : usernames) {
                for (User u : users)
                    if (n.equals(u.getUsername())) {
                        team.addUser(u);
                        break;
                    }

            }

            teams.add(team);
        }

        teams.forEach(match::addTeam);
        return teams;
    }

    private Set<Team> setTeamsToMatch(Long reservationId, Match match) throws NotFoundException {
        if (match.getTeams().isEmpty()) {
            Set<Team> teams = generateTeams(reservationId);

            for (Team team : teams) {
                match.addTeam(team);
            }
            return teams;
        } else return getActiveTeamsFromMatch(match);


    }

    private Match creteMatch() {
        Match match = new Match();
        match.setDateFrom(LocalDateTime.now());
        match.setIsActive(true);
        return match;
    }

    @Override
    public List<MatchFullResponse> findAllByReservationId(Long reservationId) throws NotFoundException {
        Set<Match> matches = getAllMatches(reservationId);
        List<MatchFullResponse> matchFullResponses = new ArrayList<>();
        for (Match m : matches) {
            MatchFullResponse matchFullResponse = new MatchFullResponse();
            matchFullResponse.setMatchId(m.getId());
            matchFullResponse.setGameResponses(gameService.getAllGamesFromMatch(m.getId()));
            matchFullResponses.add(matchFullResponse);
        }

        return matchFullResponses;
    }

    @Override
    public MatchResultsResponse getMatchResult(Long matchId) throws NotFoundException {
        matchRepository.findAll();
        Match matchById = matchRepository.findById(matchId).orElseThrow(NotFoundException::new);
        MatchResultsResponse matchResultsResponse = new MatchResultsResponse();
        matchResultsResponse.setMatchId(matchById.getId());
        matchResultsResponse.setGameResponses(gameService.getAllGamesFromMatch(matchById.getId()));
        matchResultsResponse.setTeamWinner(reservationService.getWinnerTeamByMatch(matchId));
        return matchResultsResponse;

    }


    public Set<Match> getAllMatches(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundReservationException::new);
        return reservation.getMatches();
    }


    @Override
    public Match getActiveMatch(Long reservationId) {
        Set<Match> allMatches = getAllMatches(reservationId);
        for (Match m : allMatches) {
            if (Boolean.TRUE.equals(m.getIsActive()))
                return m;
        }
        return null;
    }

    @Override
    public Set<Team> getActiveTeamsFromMatch(Match match) {
        Set<Team> teams = match.getTeams();
        Set<Team> activeTeams = new HashSet<>();
        for (Team t : teams) {
            if (Boolean.TRUE.equals(t.getActive())) {
                activeTeams.add(t);
            }
        }
        return activeTeams;

    }

    @Transactional
    @Override
    public Boolean saveResults(MatchResponseRequest matchResponseRequest) throws NotFoundException {
        matchRepository.findAll();
        Match matchById = matchRepository.findById(matchResponseRequest.getMatchId()).orElseThrow(NotFoundException::new);
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


    private void setGamesToMatch(Set<Team> teams, Match match) throws Exception {
        Game game = createGame(teams);
        match.addGame(game);
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

    private Set<Team> generateTeams(Long reservationId) throws NotFoundException {

        List<User> users = new ArrayList<>();
        Set<UserReservationEvent> userReservationEvents = reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new).getUserReservationEvents();
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

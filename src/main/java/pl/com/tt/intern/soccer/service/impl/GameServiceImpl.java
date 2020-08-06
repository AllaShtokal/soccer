package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.repository.TeamRepository;
import pl.com.tt.intern.soccer.service.GameService;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private final ButtleServiceImpl buttleService;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;
    private final ModelMapper mapper;

    //works only if set of teams is paired
    @Override
    public List<ButtleResponse> generateListOfButtlesFromListOfTeams(Set<Team> teamList) {

        Set<Buttle> buttles = new HashSet<>();
        for (Iterator<Team> it = teamList.iterator(); it.hasNext(); ) {
            Team team = it.next();
            Team team2 = it.next();
            buttles.add(new Buttle(team.getName(), team2.getName()));
        }
        return mapToButtleResponse(buttles);
    }


    @Override
    public List<TeamResponse> getSetOfWinnersByGameID(Long id) {
        Set<Team> winners = new HashSet<>();
        Set<Buttle> batlles = gameRepository.findById(id).get().getListOfButtles();
        for (Buttle battle : batlles) {
            String nameOfTheTeam = buttleService.getTeamWinner(battle);
            winners.add(teamRepository.findByName(nameOfTheTeam).get());
        }

        return mapToTeamResponse(winners);
    }


    //TODO: change next 4 methods to 2 <T>templates methods to shortcut the code
    private List<ButtleResponse> mapToButtleResponse(Set<Buttle> buttles) {
        return buttles.stream()
                .map(this::mapToButtleResponse)
                .collect(toList());
    }

    private ButtleResponse mapToButtleResponse(Buttle buttle) {
        return mapper.map(buttle, ButtleResponse.class);
    }

    private List<TeamResponse> mapToTeamResponse(Set<Team> teams) {
        return teams.stream()
                .map(this::mapToTeamResponse)
                .collect(toList());
    }

    private TeamResponse mapToTeamResponse(Team team) {
        return mapper.map(team, TeamResponse.class);
    }
}

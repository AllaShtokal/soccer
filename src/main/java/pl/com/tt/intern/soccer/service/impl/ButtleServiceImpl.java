package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.SameResultsException;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;
import pl.com.tt.intern.soccer.repository.ButtleRepository;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.TeamService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ButtleServiceImpl implements ButtleService {

    private final GameRepository gameRepository;
    private final ButtleRepository buttleRepository;
    private final ModelMapper modelMapper;
    private final TeamService teamService;

    @Override
    public Set<String> getSetOfNamesOfTeamWinners(Set<Buttle> buttles) {
        Set<String> activeTeamsNames = new HashSet<>();
        for (Buttle b : buttles) {
            activeTeamsNames.add(getTeamNameWinner(b));
        }

        return activeTeamsNames;
    }

    private String getTeamNameWinner(Buttle buttle) {
        if(buttle.getScoreTeam1() == buttle.getScoreTeam2()){
            throw new SameResultsException();
        }else
        if (buttle.getScoreTeam1() > buttle.getScoreTeam2())
            return buttle.getTeamName1();
        else return buttle.getTeamName2();
    }
    @Override
    public TeamResponse getTeamWinner(ButtleResponse buttle, Long matchId) {

        TeamResponse teamResponse = new TeamResponse();
        if (buttle.getScoreTeam1() > buttle.getScoreTeam2()){
            teamResponse.setTeam_id(teamService.getTeamIdByTeamNameAndMatchId(buttle.getTeamName1(), matchId));
            teamResponse.setName(buttle.getTeamName1());
            teamResponse.setUsers(teamService.getUsersByTeamNameAndMatchId(buttle.getTeamName1(),  matchId));
        }
        else {
            teamResponse.setTeam_id(teamService.getTeamIdByTeamNameAndMatchId(buttle.getTeamName2(), matchId));
            teamResponse.setName(buttle.getTeamName2());
            teamResponse.setUsers(teamService.getUsersByTeamNameAndMatchId(buttle.getTeamName2(), matchId));
        }


        return teamResponse;
    }

    @Override
    public void setActiveTeamsByListOfButtles(Set<Team> teams, Set<Buttle> buttles) {

        Set<String> activeTeamsNames = getSetOfNamesOfTeamWinners(buttles);
        for (Team t : teams) {
            if (activeTeamsNames.contains(t.getName())) {
                t.getUsers().forEach(user -> user.getUserInfo().addWon());
            }

            else {
                t.setActive(false);
                t.getUsers().forEach(user -> user.getUserInfo().addLost());}
        }

    }

    @Override
    public void save(Buttle buttle) {
        buttleRepository.save(buttle);

    }

    @Override
    public List<ButtleResponse> getAllButtlesByGameID(Long gameId) throws NotFoundException {
        Game gameById = gameRepository.findById(gameId).orElseThrow(NotFoundException::new);
        Set<Buttle> buttles = gameById.getButtles();
        List<ButtleResponse> buttleResponses = new ArrayList<>();
        for(Buttle b: buttles)
        {
            ButtleResponse buttleResponse = modelMapper.map(b, ButtleResponse.class);
            buttleResponses.add(buttleResponse);
        }
        return buttleResponses;


    }


}

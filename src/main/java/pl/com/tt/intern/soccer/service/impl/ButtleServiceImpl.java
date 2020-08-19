package pl.com.tt.intern.soccer.service.impl;

import com.sun.org.apache.xpath.internal.operations.Bool;
import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Match;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.MatchService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ButtleServiceImpl implements ButtleService {

    private final GameRepository gameRepository;
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
        if (buttle.getScoreTeam1() > buttle.getScoreTeam2())
            return buttle.getTeamName1();
            //if first and second is the same?????! for now it also returns second team
        else return buttle.getTeamName2();
    }
    @Override
    public TeamResponse getTeamWinner(ButtleResponse buttle) {

        TeamResponse teamResponse = new TeamResponse();
        if (buttle.getScoreTeam1() > buttle.getScoreTeam2()){
            teamResponse.setTeam_id(teamService.getTeamIdByTeamName(buttle.getTeamName1()));
            teamResponse.setName(buttle.getTeamName1());
            teamResponse.setUsers(teamService.getUsersByTeamName(buttle.getTeamName1()));
        }

        //if first and second is the same?????! for now it also returns second team
        else {
            teamResponse.setTeam_id(teamService.getTeamIdByTeamName(buttle.getTeamName2()));
            teamResponse.setName(buttle.getTeamName2());
            teamResponse.setUsers(teamService.getUsersByTeamName(buttle.getTeamName2()));
        }


        return teamResponse;
    }

    @Override
    public void setActiveTeamsByListOfButtles(Set<Team> teams, Set<Buttle> buttles) {

        Set<String> activeTeamsNames = getSetOfNamesOfTeamWinners(buttles);
        for (Team t : teams) {
            if (!activeTeamsNames.contains(t.getName())) {
                t.setActive(false);
            }
        }

    }

    @Override
    public List<ButtleResponse> getAllButtlesByGameID(Long game_id) {
        Game gameById = gameRepository.findById(game_id).get();
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

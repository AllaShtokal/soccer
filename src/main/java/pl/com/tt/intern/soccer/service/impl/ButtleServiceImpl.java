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
import pl.com.tt.intern.soccer.repository.GameRepository;
import pl.com.tt.intern.soccer.service.ButtleService;
import pl.com.tt.intern.soccer.service.MatchService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ButtleServiceImpl implements ButtleService {

    private final GameRepository gameRepository;
    private final ModelMapper modelMapper;

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

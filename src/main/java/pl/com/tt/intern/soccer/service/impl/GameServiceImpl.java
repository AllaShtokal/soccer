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
            Team team = it.next();
            Team team2 = it.next();
            buttles.add(new Buttle(team.getName(), team2.getName()));
        }
        return buttles;

    }

}

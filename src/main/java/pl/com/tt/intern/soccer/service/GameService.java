package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.ButtleResponse;
import pl.com.tt.intern.soccer.payload.response.TeamResponse;

import java.util.List;
import java.util.Set;

public interface GameService {


    Set<Buttle> generateListOfButtlesFromListOfTeams(Set<Team> teamList);
}

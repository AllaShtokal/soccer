package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;

public interface ButtleService {

    Team getTeamWinner(Buttle buttle);
}

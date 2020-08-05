package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;

import java.util.List;
import java.util.Set;

public interface MatchService {

    Game createGameByMatchId(Long id);
}

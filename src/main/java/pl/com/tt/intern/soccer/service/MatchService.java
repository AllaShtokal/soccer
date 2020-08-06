package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.payload.response.MatchResponse;

import java.util.List;
import java.util.Set;

public interface MatchService {

    MatchResponse create (Long reservation_id);

}

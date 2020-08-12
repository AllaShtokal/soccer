package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Game;

import java.util.List;
import java.util.Set;

@Data
public class MatchResponseRequest {
    private Long matchId;
    private List<ButtleResponse> activeButtles;


}


package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import java.util.List;

@Data
public class MatchResponseRequest {
    private Long matchId;
    private List<ButtleResponse> activeButtles;


}


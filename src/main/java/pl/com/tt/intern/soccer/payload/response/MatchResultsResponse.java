package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class MatchResultsResponse {

    private Long matchId;
    private TeamResponse teamWinner;
    private List<GameResponse> gameResponses;


}

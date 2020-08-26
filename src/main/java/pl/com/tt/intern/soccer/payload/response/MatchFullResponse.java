package pl.com.tt.intern.soccer.payload.response;


import lombok.Data;

import java.util.List;

@Data
public class MatchFullResponse {

    private Long matchId;
    private Boolean isActive;
    private List<GameResponse> gameResponses;

}

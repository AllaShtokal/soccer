package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

import java.util.List;

@Data
public class GameResponse {

    private Long gameId;
    private List<ButtleResponse> buttles;
}

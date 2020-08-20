package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

@Data
public class LobbyResponse {
    private String name;

    public LobbyResponse(){}
    public LobbyResponse(String name) {
        this.name = name;
    }

}

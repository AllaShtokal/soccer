package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;
@Data
public class LobbyChangeRequest {

    private String name;

    private Boolean available;

    private  Long limit;
}

package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LobbyRequest {

    @NotNull
    private String name;
    @NotNull
    private Boolean available;
    @NotNull
    private  Long limit;
}

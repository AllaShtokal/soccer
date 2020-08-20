package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationPersistRequest {

    @NotNull
    private LocalDateTime dateFrom;

    @NotNull
    private LocalDateTime dateTo;

    @NotNull
    private String lobbyName;

}

package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class ReservationSimpleDateRequest {

    @NotNull
    private LocalDateTime from;

    @NotNull
    private LocalDateTime to;

}

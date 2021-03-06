package pl.com.tt.intern.soccer.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationDateRequest {

    @NotNull
    private LocalDateTime from;

    @NotNull
    private LocalDateTime to;

}

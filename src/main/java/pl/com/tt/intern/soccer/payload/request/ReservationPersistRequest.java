package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationPersistRequest {

    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

}

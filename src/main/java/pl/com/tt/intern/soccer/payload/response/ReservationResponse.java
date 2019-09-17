package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.Reservation;

import java.time.LocalDateTime;

@Data
public class ReservationResponse {

    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;

    public ReservationResponse(Reservation reservation) {
        this.userId = reservation.getUser().getId();
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.confirmed = reservation.getConfirmed();
    }

}

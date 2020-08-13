package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.Reservation;

import java.time.LocalDateTime;

@Data
public class ReservationShortInfoResponse {

    private Long reservationId;
    private String username;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;
    private String lobbyName;
    private String LimitTaken;

    public ReservationShortInfoResponse() {
    }

    public ReservationShortInfoResponse(Reservation reservation) {
        this.reservationId = reservation.getId();
        this.username = reservation.getUser().getUsername();
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.confirmed = reservation.getConfirmed();
        this.LimitTaken = reservation.getLobby().getLimit()
                + "/"
                + reservation.getUserReservationEvents().size();
    }
}

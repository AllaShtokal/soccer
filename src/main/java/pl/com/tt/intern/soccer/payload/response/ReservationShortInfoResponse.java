package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.UserReservationEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ReservationShortInfoResponse {

    private Long reservationId;
    private String username;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;
    private String lobbyName;
    private String LimitTaken; //todo correct
    private Boolean isAttached;
    private List<BasicUserInfoResponse> users = new ArrayList<>();

    public ReservationShortInfoResponse() {
    }

    public ReservationShortInfoResponse(Reservation reservation, Long userId) {
        this.reservationId = reservation.getId();
        this.username = reservation.getUser().getUsername();
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.confirmed = reservation.getConfirmed();
        this.lobbyName = reservation.getLobby().getName();

        this.LimitTaken = reservation.getLobby().getLimit()
                + "/"
                + reservation.getUserReservationEvents().size();

        Set<UserReservationEvent> userReservationEvents = reservation.getUserReservationEvents();
        for (UserReservationEvent e : userReservationEvents) {
            if (e.getUser().getId().equals(userId)) {
                this.isAttached = true;
                break;
            }
        }

        for (UserReservationEvent e : userReservationEvents) {
            BasicUserInfoResponse basicUserInfoResponse = new BasicUserInfoResponse();

            basicUserInfoResponse.setEmail(e.getUser().getEmail());
            basicUserInfoResponse.setUsername(e.getUser().getUsername());

            users.add(basicUserInfoResponse);
        }
    }
}

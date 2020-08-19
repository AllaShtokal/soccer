package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
import pl.com.tt.intern.soccer.model.enums.ReservationStatus;
import sun.font.DelegatingShape;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static pl.com.tt.intern.soccer.model.enums.ReservationStatus.*;

@Data
public class MyReservationResponse {

    private Long reservationId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;
    private String lobbyName;
    private String LimitTaken;
    private Boolean isAttached;
    private ReservationStatus status;
    private List<BasicUserInfoResponse> users = new ArrayList<>();

    public MyReservationResponse() {
    }

    public MyReservationResponse(Reservation reservation, Long userId) {

        LocalDateTime localDateTime = LocalDateTime.now();
        if (reservation.getDateTo().isAfter(localDateTime)) {
            reservation.getMatches().forEach(match -> {
                        if (match.getIsActive()) this.status = PRESENT;
                    }
            );

         if(this.status==null) this.status = FUTURE;
        } else this.status = PAST;


        this.reservationId = reservation.getId();
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

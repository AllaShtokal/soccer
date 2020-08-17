package pl.com.tt.intern.soccer.payload.response;

import com.sun.org.apache.xpath.internal.operations.Mod;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
public class ReservationResponse {

    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;
    private String lobbyName;

    public ReservationResponse() {
    }

    public ReservationResponse(Reservation reservation) {
        this.userId = reservation.getUser().getId();
        this.dateFrom = reservation.getDateFrom();
        this.dateTo = reservation.getDateTo();
        this.confirmed = reservation.getConfirmed();
        this.lobbyName = reservation.getLobby().getName();



    }


}

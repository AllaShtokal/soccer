package pl.com.tt.intern.soccer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRetrieveDTO {

    private int id;
    // TODO: 16.09.2019 consider implementing UserResponse (userinformation)
//    private UserResponse user;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;

}

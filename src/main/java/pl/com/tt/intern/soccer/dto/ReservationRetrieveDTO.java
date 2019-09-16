package pl.com.tt.intern.soccer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationRetrieveDTO {

    private int id;
//    private UserResponse user;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;

}

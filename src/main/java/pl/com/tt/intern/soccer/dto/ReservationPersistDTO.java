package pl.com.tt.intern.soccer.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationPersistDTO {

    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

}

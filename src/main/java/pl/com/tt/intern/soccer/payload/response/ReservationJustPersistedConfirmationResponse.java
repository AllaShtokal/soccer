package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReservationJustPersistedConfirmationResponse {

    private Long id;
    private Long userId;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;
    private Boolean confirmed;

}

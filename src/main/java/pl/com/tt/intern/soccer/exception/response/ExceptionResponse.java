package pl.com.tt.intern.soccer.exception.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ExceptionResponse {

    private final String message;
    private final Short status;
    private final LocalDateTime time;

}

package pl.com.tt.intern.soccer.exception.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class ValidationResponse {

    private final Map<String, String> validation;
    private final Short status;
    private final LocalDateTime time;

}

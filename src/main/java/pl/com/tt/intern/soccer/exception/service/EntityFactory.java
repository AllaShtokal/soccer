package pl.com.tt.intern.soccer.exception.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.response.ExceptionResponse;
import pl.com.tt.intern.soccer.exception.response.ValidationResponse;

import java.util.Map;

import static java.time.LocalDateTime.now;
import static org.springframework.http.ResponseEntity.status;

@Service
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityFactory {

    public static ResponseEntity<ExceptionResponse> entity(String message, HttpStatus status) {
        return status(status)
                .body(ResponseFactory.response(message, status));
    }

    public static ResponseEntity<ValidationResponse> entity(Map<String, String> validationMap, HttpStatus status) {
        return status(status)
                .body(ResponseFactory.response(validationMap, status));
    }

    private static class ResponseFactory {

        public static ExceptionResponse response(String message, HttpStatus status) {
            return ExceptionResponse.builder()
                    .message(message)
                    .status((short) status.value())
                    .time(now())
                    .build();
        }

        public static ValidationResponse response(Map<String, String> validationMap, HttpStatus status) {
            return ValidationResponse.builder()
                    .validation(validationMap)
                    .status((short) status.value())
                    .time(now())
                    .build();
        }
    }
}

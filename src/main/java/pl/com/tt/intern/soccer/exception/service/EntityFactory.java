package pl.com.tt.intern.soccer.exception.service;

import com.wojcik.music.exception.response.ExceptionResponse;
import com.wojcik.music.exception.response.ValidationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@Service
public class EntityFactory {

    public static ResponseEntity entity(String message, HttpStatus status) {
        return status(status)
                .body(ResponseFactory.response(message, status));
    }

    public static ResponseEntity entity(Map<String, String> validationMap, HttpStatus status) {
        return status(status)
                .body(ResponseFactory.response(validationMap, status));
    }

    private static class ResponseFactory {

        public static ExceptionResponse response(String message, HttpStatus status) {
            return ExceptionResponse.builder()
                    .message(message)
                    .status((short) status.value())
                    .time(LocalDateTime.now())
                    .build();
        }

        public static ValidationResponse response(Map validationMap, HttpStatus status) {
            return ValidationResponse.builder()
                    .validation(validationMap)
                    .status((short) status.value())
                    .time(LocalDateTime.now())
                    .build();
        }
    }
}

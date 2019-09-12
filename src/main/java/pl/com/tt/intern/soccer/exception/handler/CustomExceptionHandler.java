package pl.com.tt.intern.soccer.exception.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.exception.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static pl.com.tt.intern.soccer.exception.service.EntityFactory.entity;
import static pl.com.tt.intern.soccer.exception.service.ValidationService.validate;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> general(Exception e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFound(NotFoundException e) {
        return entity(e.getMessage(), NOT_FOUND);
    }

    @Override
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                       HttpHeaders headers, HttpStatus status,
                                                       WebRequest request) {
        return entity(validate(e.getBindingResult()), BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> duplicateEntry(DataIntegrityViolationException e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<ExceptionResponse> duplicateEntry(PasswordsMismatchException e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

}

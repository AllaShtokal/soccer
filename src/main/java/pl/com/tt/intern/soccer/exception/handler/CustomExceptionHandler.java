package pl.com.tt.intern.soccer.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.exception.response.ExceptionResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static pl.com.tt.intern.soccer.exception.service.EntityFactory.entity;
import static pl.com.tt.intern.soccer.exception.service.ValidationService.validate;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException e,
                                                       HttpHeaders headers, HttpStatus status,
                                                       WebRequest request) {
        return entity(validate(e.getBindingResult()), BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> general(Exception e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> notFound(NotFoundException e) {
        return entity(e.getMessage(), NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionResponse> duplicateEntry(DataIntegrityViolationException e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(PasswordsMismatchException.class)
    public ResponseEntity<ExceptionResponse> passwordMismatch(PasswordsMismatchException e) {
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(ActivationAccountException.class)
    public ResponseEntity<ExceptionResponse> accountActivationError(ActivationAccountException e) {
        log.error("Throw ActivationAccountException with message: {}", e.getMessage());
        return entity(e.getMessage(), BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ExceptionResponse> nullPointer(NullPointerException e) {
        log.error("Throw NullPointerException with message: {}", e.getMessage());
        return entity("Wrong input", BAD_REQUEST);
    }

}

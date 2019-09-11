package pl.com.tt.intern.soccer.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.BindingResult;

public class ValidationException extends Exception {

    @Getter
    @Setter
    private BindingResult result;

    public ValidationException(BindingResult result) {
        super("validation failed");
        this.result = result;
    }

    public ValidationException(String message, BindingResult result) {
        super(message);
        this.result = result;
    }

}

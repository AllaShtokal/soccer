package pl.com.tt.intern.soccer.exception;

public class NotFoundException extends Exception {

    public NotFoundException() {
        super("resource not found");
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundException(Throwable cause) {
        super(cause);
    }
}

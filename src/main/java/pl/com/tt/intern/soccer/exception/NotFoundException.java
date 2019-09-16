package pl.com.tt.intern.soccer.exception;

public class NotFoundException extends Exception {

    private static final long serialVersionUID = -5471095360918080692L;

    public NotFoundException() {
        super("Resource not found");
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

package pl.com.tt.intern.soccer.exception;

public class PasswordsMismatchException extends Exception {

    private static final long serialVersionUID = -6723976434883707005L;

    public PasswordsMismatchException() {
        super("Passwords do not match");
    }

    public PasswordsMismatchException(String message) {
        super(message);
    }

    public PasswordsMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordsMismatchException(Throwable cause) {
        super(cause);
    }
}

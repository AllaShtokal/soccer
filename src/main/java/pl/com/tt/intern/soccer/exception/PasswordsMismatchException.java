package pl.com.tt.intern.soccer.exception;

public class PasswordsMismatchException extends Exception {

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

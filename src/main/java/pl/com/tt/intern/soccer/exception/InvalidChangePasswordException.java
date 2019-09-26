package pl.com.tt.intern.soccer.exception;

public class InvalidChangePasswordException extends Exception {
    private static final long serialVersionUID = -6502848849688088514L;

    public InvalidChangePasswordException() {
    }

    public InvalidChangePasswordException(String message) {
        super(message);
    }
}

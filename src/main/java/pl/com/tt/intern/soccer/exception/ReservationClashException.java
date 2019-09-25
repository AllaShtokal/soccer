package pl.com.tt.intern.soccer.exception;

public class ReservationClashException extends Exception {

    public ReservationClashException(String message) {
        super(message);
    }

    public ReservationClashException() {
    }
}

package pl.com.tt.intern.soccer.exception;

public class NotFoundReservationException extends RuntimeException {
    public NotFoundReservationException() {
        super("Reservation by id not found ");
    }
}

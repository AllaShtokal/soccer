package pl.com.tt.intern.soccer.exception;

public class UserAlreadyExistException extends RuntimeException{

    public UserAlreadyExistException(Long userId,Long reservationId) {
        super("User id: " + userId+ " already exists in this Reservation id: " + reservationId );
    }
}

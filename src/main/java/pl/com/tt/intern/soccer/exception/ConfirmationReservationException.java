package pl.com.tt.intern.soccer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ConfirmationReservationException extends Exception {

    private static final long serialVersionUID = 8964159968289894286L;

    public ConfirmationReservationException (String message){
        super(message);
    }
}

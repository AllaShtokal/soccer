package pl.com.tt.intern.soccer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IncorrectConfirmationKeyException extends Exception {

    private static final long serialVersionUID = -2627978321393644274L;

    public IncorrectConfirmationKeyException(String message) {
        super(message);
    }
}

package pl.com.tt.intern.soccer.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CorrectTokenException extends Exception {

    private static final long serialVersionUID = -2627978321393644274L;

    public CorrectTokenException(String message) {
        super(message);
    }
}

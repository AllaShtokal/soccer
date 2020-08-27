package pl.com.tt.intern.soccer.exception;

public class SameResultsException extends RuntimeException{

    public SameResultsException() {
        super("Results could not be the same!");
    }
}

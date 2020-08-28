package pl.com.tt.intern.soccer.exception;

public class NumberOfTeamsException extends RuntimeException {
    public NumberOfTeamsException() {
        super("number of teams should be 2 or 4 or 8 or 16 etc (^ of 2) ");
    }
}

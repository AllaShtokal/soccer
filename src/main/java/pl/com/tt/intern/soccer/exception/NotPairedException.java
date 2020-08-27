package pl.com.tt.intern.soccer.exception;

public class NotPairedException extends RuntimeException {
    public  NotPairedException(){
        super(" number of teams is NOT paired ");
    }
}

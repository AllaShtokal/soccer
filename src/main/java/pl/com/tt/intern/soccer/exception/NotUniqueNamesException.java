package pl.com.tt.intern.soccer.exception;

public class NotUniqueNamesException extends RuntimeException {
    public NotUniqueNamesException (){
        super(" names are not unique! ");
    }
}

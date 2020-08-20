package pl.com.tt.intern.soccer.exception;

public class NotFoundLobbyByIdException extends RuntimeException{

    public NotFoundLobbyByIdException(String id) {
        super("Lobby by name not found, id: " + id);
    }

}

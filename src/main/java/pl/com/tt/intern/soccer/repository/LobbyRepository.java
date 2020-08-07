package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.tt.intern.soccer.model.Lobby;

public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    Lobby findFirstByName (String name);
}

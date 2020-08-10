package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Lobby;

import java.util.Optional;


public interface LobbyRepository extends JpaRepository<Lobby, Long> {

    Optional<Lobby> findFirstByName (String name);
}

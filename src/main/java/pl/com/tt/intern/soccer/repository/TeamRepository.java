package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Game;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.model.User;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);
}

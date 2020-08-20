package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    Team findByName(String name);
}

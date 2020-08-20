package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}

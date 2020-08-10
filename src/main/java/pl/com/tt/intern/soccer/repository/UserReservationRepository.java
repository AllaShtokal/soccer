package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
@Repository
public interface UserReservationRepository extends JpaRepository<UserReservationEvent, Long> {
}

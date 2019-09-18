package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByIdAndUserId(Long id, Long userId);

}

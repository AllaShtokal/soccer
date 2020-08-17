package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
@Repository
public interface UserReservationRepository extends JpaRepository<UserReservationEvent, Long> {

    @Modifying
    @Query("delete from UserReservationEvent b where b.id=:id")
    void deleteByid (Long id);
}

package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByDateToAfterAndDateFromBefore(LocalDateTime from, LocalDateTime to);

    List<Reservation> findAllByDateFromAfterAndDateToBefore(LocalDateTime from, LocalDateTime to);

    List<Reservation> findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(LocalDateTime from, LocalDateTime to);
    List<Reservation> findAllByUserId(Long user_id);


    boolean existsByIdAndUserId(Long id, Long userId);

    @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE " +
            "r.dateFrom < :dateTo " +
            "AND r.dateTo > :dateFrom " +
            "AND r.id != :excludedReservationId ")
    boolean datesCollideExcludingCurrent(@Param("dateFrom") LocalDateTime dateFrom,
                                         @Param("dateTo") LocalDateTime dateTo,
                                         @Param("excludedReservationId") Long excludedReservationId);


    @Query(value = "SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Reservation r WHERE r.dateFrom < :dateTo AND r.dateTo > :dateFrom")
    boolean datesCollide(@Param("dateFrom") LocalDateTime dateFrom, @Param("dateTo") LocalDateTime dateTo);
}

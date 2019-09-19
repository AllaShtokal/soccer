package pl.com.tt.intern.soccer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;

import java.util.List;

@Repository
public interface ConfirmationReservationRepository extends JpaRepository<ConfirmationReservation, Long> {

    List<ConfirmationReservation> findAllByEmailSent(Boolean emailSent);
}

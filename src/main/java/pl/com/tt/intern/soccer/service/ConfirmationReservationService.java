package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;

import java.util.List;

public interface ConfirmationReservationService {
    ConfirmationReservation save(ConfirmationReservation confirmationReservation);

    List<ConfirmationReservation> findAllByEmailSend(Boolean isSend);

    ConfirmationReservation findConfirmationReservationByUUID(String uuid);

    void createAndSaveConfirmationReservation(Reservation reservation);
}

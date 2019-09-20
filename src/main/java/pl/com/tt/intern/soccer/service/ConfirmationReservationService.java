package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.ConfirmationReservation;

import java.util.List;

public interface ConfirmationReservationService {

    void saveAndAddConfirmationReservationToTaskTimer(ConfirmationReservation confirmationReservation);

    ConfirmationReservation save(ConfirmationReservation confirmationReservation);

    List<ConfirmationReservation> findAllByEmailSent(Boolean mailSent);

    List<ConfirmationReservation> findAll();
}

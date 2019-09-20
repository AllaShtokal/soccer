package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForConfirmationReservation;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;

public interface ConfirmationKeyForConfirmationReservationService {

    ConfirmationKeyForConfirmationReservation save(ConfirmationKeyForConfirmationReservation token);

    ConfirmationKeyForConfirmationReservation findConfirmationKeyByUuid(String uuid) throws NotFoundException;

    void scanAndDeleteExpiredConfirmationKeys();

}

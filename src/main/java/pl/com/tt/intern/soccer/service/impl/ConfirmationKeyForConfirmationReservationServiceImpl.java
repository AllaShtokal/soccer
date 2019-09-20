package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForConfirmationReservation;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;
import pl.com.tt.intern.soccer.repository.ConfirmationKeyForConfirmationReservationRepository;
import pl.com.tt.intern.soccer.repository.ConfirmationKeyForSignUpRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyForConfirmationReservationService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyForSignUpService;

@Service
@RequiredArgsConstructor
public class ConfirmationKeyForConfirmationReservationServiceImpl implements ConfirmationKeyForConfirmationReservationService {

    private final ConfirmationKeyForConfirmationReservationRepository confirmationKeyForConfirmationReservationRepository;

    @Override
    public ConfirmationKeyForConfirmationReservation save(ConfirmationKeyForConfirmationReservation confirmationKeyForConfirmationReservation) {
        return confirmationKeyForConfirmationReservationRepository.save(confirmationKeyForConfirmationReservation);
    }

    @Override
    public ConfirmationKeyForConfirmationReservation findConfirmationKeyByUuid(String uuid) throws NotFoundException {
        return confirmationKeyForConfirmationReservationRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Token not found in database."));
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void scanAndDeleteExpiredConfirmationKeys() {
        confirmationKeyForConfirmationReservationRepository.deleteByExpirationTime();
    }

}

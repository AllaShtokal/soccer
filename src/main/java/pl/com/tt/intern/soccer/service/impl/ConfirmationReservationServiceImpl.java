package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.repository.ConfirmationReservationRepository;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;

import java.util.List;
import java.util.Timer;

@Service
@RequiredArgsConstructor
public class ConfirmationReservationServiceImpl implements ConfirmationReservationService {

    private final ConfirmationReservationRepository repository;
   // private final Timer timer;


    @Override
    public ConfirmationReservation save(ConfirmationReservation confirmationReservation) {
       // timer.schedule(sendMail(), confirmationReservation.getTimeToSend());
        return repository.save(confirmationReservation);
    }

    @Override
    public List<ConfirmationReservation> findAllByEmailSent(Boolean mailSent) {
        return repository.findAllByEmailSent(mailSent);
    }

    @Override
    public List<ConfirmationReservation> findAll() {
        return repository.findAll();
    }
}

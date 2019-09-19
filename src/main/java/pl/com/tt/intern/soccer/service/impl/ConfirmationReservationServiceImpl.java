package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.repository.ConfirmationReservationRepository;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class ConfirmationReservationServiceImpl implements ConfirmationReservationService {

    private final ConfirmationReservationRepository repository;
    private final Timer timer;

    @Override
    public void save(ConfirmationReservation confirmationReservation) {
        repository.save(confirmationReservation);
    }

    @Override
    public List<ConfirmationReservation> findAllByEmailSent(Boolean mailSent) {
        return repository.findAllByEmailSent(mailSent);
    }

    @Override
    public List<ConfirmationReservation> findAll() {
        return repository.findAll();
    }

    @Override
    public void saveAndAddConfirmationReservationToTaskTimer(ConfirmationReservation confirmationReservation){
        save(confirmationReservation);
        addTaskToMailSender(confirmationReservation);
    }

    private void addTaskToMailSender(ConfirmationReservation confirmationReservation){
        timer.schedule(getTimerTask(), DateUtil.toDate(confirmationReservation.getTimeToSend()));
    }

    private TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("mail został wysłany");
            }
        };
    }
}

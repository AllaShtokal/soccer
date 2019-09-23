package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;
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
    public ConfirmationReservation save(ConfirmationReservation confirmationReservation) {
        return repository.save(confirmationReservation);
    }

    @Override
    public List<ConfirmationReservation> findAllByEmailSend(Boolean isSend) {
        return repository.findAllByEmailSent(isSend);
    }

    @Override
    public void createAndSaveConfirmationReservation(Reservation reservation) {
        ConfirmationReservation confirmationReservation = generateConfirmationReservation(reservation);
        repository.save(confirmationReservation);
        addTaskToTimerTask(confirmationReservation);

    }

    private ConfirmationReservation generateConfirmationReservation(Reservation reservation) {
        return new ConfirmationReservation(reservation);
    }

    private void addTaskToTimerTask(ConfirmationReservation confirmationReservation) {
        timer.schedule(getTimerTask(confirmationReservation), DateUtil.toDate(confirmationReservation.getTimeToMailSend()));
    }

    private TimerTask getTimerTask(ConfirmationReservation confirmationReservation){
        return new TimerTask() {
            @Override
            public void run() {
                //wysyłka maila
            }
        };
    }
}

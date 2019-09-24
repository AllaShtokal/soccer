package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
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
    private final ChangeAccountMailFactory mailFactory;
    private final ChangeAccountUrlGeneratorFactory urlFactory;

    @Override
    public List<ConfirmationReservation> findAll(){
        return repository.findAll();
    }

    @Override
    public ConfirmationReservation save(ConfirmationReservation confirmationReservation) {
        return repository.save(confirmationReservation);
    }

    @Override
    public List<ConfirmationReservation> findAllByEmailSend(Boolean isSend) {
        return repository.findAllByEmailSent(isSend);
    }

    @Override
    public ConfirmationReservation findConfirmationReservationByUUID(String uuid){
        return repository.findByUuid(uuid);
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
                simulateMailSend(confirmationReservation);
            }
        };
    }

    private void simulateMailSend(ConfirmationReservation confirmationReservation){
        String url = urlFactory.getUrlGenerator(AccountChangeType.CONFIRM_RESERVATION)
                .generate(confirmationReservation.getReservation().getUser().getEmail(), confirmationReservation.getUuid());
        mailFactory.getMailSender(AccountChangeType.CONFIRM_RESERVATION)
                .send(confirmationReservation.getReservation().getUser().getEmail(), url);
        confirmEmailSent(confirmationReservation);
    }

    private void confirmEmailSent (ConfirmationReservation confirmationReservation){
        confirmationReservation.setEmailSent(true);
        repository.save(confirmationReservation);
    }
}
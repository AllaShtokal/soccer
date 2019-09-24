package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.repository.ConfirmationReservationRepository;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;

import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class ConfirmationReservationServiceImpl implements ConfirmationReservationService {

    private final ConfirmationReservationRepository repository;
    private final Timer timer;
    private final ChangeAccountMailFactory mailFactory;
    private final ChangeAccountUrlGeneratorFactory urlFactory;
    private final ReservationRepository reservationRepository;


    @Override
    public List<ConfirmationReservation> findAll() {
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
    public ConfirmationReservation findConfirmationReservationByUUID(String uuid) {
        return repository.findByUuid(uuid);
    }

    @Override
    public void createAndSaveConfirmationReservation(Reservation reservation) throws NotFoundException {
        ConfirmationReservation confirmationReservation = generateConfirmationReservation(reservation);
        repository.save(confirmationReservation);
        addTaskToTimerTask(confirmationReservation);
        addRemoveReservationTaskToTimerTask(confirmationReservation);
    }

    private ConfirmationReservation generateConfirmationReservation(Reservation reservation) {
        return new ConfirmationReservation(reservation);
    }

    private void addTaskToTimerTask(ConfirmationReservation confirmationReservation) {
        timer.schedule(getTimerTask(confirmationReservation), DateUtil.toDate(confirmationReservation.getTimeToMailSend()));
    }

    private TimerTask getTimerTask(ConfirmationReservation confirmationReservation) {
        return new TimerTask() {
            @Override
            public void run() {
                MailSendTask(confirmationReservation);
            }
        };
    }

    private void MailSendTask(ConfirmationReservation confirmationReservation) {
        String url = urlFactory.getUrlGenerator(AccountChangeType.CONFIRM_RESERVATION)
                .generate(confirmationReservation.getReservation().getUser().getEmail(), confirmationReservation.getUuid());
        mailFactory.getMailSender(AccountChangeType.CONFIRM_RESERVATION)
                .send(confirmationReservation.getReservation().getUser().getEmail(), url);
        confirmEmailSent(confirmationReservation);
    }

    private void confirmEmailSent(ConfirmationReservation confirmationReservation) {
        confirmationReservation.setEmailSent(true);
        repository.save(confirmationReservation);
    }

    private void addRemoveReservationTaskToTimerTask(ConfirmationReservation confirmationReservation) throws NotFoundException {
        timer.schedule(getTimerTask(getReservation(confirmationReservation.getReservation().getId())), DateUtil.toDate(confirmationReservation.getExpirationTime().plusMinutes(1)));
    }

    private Reservation getReservation(Long id) throws NotFoundException {
        Optional<Reservation> optionalReservation = reservationRepository.findById(id);
        if (optionalReservation.isPresent()) {
            return optionalReservation.get();
        } else {
            throw new NotFoundException();
        }
    }

    private TimerTask getTimerTask(Reservation reservation) {
        return new TimerTask() {
            @Override
            public void run() {
                if (!reservation.getConfirmed()) {
                    reservationRepository.deleteById(reservation.getId());
                }
            }
        };
    }
}
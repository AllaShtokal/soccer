package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import pl.com.tt.intern.soccer.util.CustomDateUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public void createAndSaveConfirmationReservation(Reservation reservation) {
        ConfirmationReservation confirmationReservation = generateConfirmationReservation(reservation);
        repository.save(confirmationReservation);
        addTaskToTimerTask(confirmationReservation);
        addRemoveReservationTaskToTimerTask(confirmationReservation);
    }

    private ConfirmationReservation generateConfirmationReservation(Reservation reservation) {
        return new ConfirmationReservation(reservation);
    }

    private void addTaskToTimerTask(ConfirmationReservation confirmationReservation) {
        timer.schedule(getTimerTask(confirmationReservation), CustomDateUtil.toDate(confirmationReservation.getTimeToMailSend()));
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

    private void addRemoveReservationTaskToTimerTask(ConfirmationReservation confirmationReservation) {
        try {
            timer.schedule(getTimerTask(getReservation(confirmationReservation.getReservation().getId())), CustomDateUtil.toDate(confirmationReservation.getExpirationTime().plusMinutes(1)));
        } catch (NotFoundException e) {
            log.error("Reservation can't be found in database");
        }
    }

    private Reservation getReservation(Long id) throws NotFoundException {
        return reservationRepository.findById(id).orElseThrow(NotFoundException::new);
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
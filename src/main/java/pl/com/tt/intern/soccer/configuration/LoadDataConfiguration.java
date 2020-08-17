package pl.com.tt.intern.soccer.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.util.CustomDateUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.CONFIRM_RESERVATION;
import static pl.com.tt.intern.soccer.account.url.enums.UrlParam.CONFIRMATION_RESERVATION_KEY;

@Configuration
@RequiredArgsConstructor
public class LoadDataConfiguration {

    @Value("${mail.config.enabled}")
    private Boolean isEnabled;

    private final ConfirmationReservationService confirmationReservationService;
    private final Timer timer;
    private final ReservationService reservationService;
    private final ChangeAccountMailFactory mailFactory;
    private final ChangeAccountUrlGeneratorFactory urlFactory;


    @EventListener(ApplicationReadyEvent.class)
    public void addConfirmationsToList() {
        if (isEnabled) {
            confirmationReservationService.findAllByEmailSend(false).stream()
                    .filter(cr -> cr.getTimeToMailSend().isAfter(LocalDateTime.now()))
                    .forEach(cr -> timer.schedule(getNewTimerTask(cr), CustomDateUtil.toDate(cr.getTimeToMailSend())));
        }
    }

    private TimerTask getNewTimerTask(ConfirmationReservation cr) {
        return new TimerTask() {
            @Override
            public void run() {
                Map<UrlParam, String> params = new HashMap<>();
                params.put(CONFIRMATION_RESERVATION_KEY, cr.getUuid());

                String url = urlFactory
                        .getUrlGenerator(CONFIRM_RESERVATION)
                        .generate(params);

                mailFactory
                        .getMailSender(CONFIRM_RESERVATION)
                        .send(
                                cr
                                        .getReservation()
                                        .getUser()
                                        .getEmail(),
                                url
                        );

                setMailSend(cr);
            }
        };
    }

    private void setMailSend(ConfirmationReservation cr) {
        cr.setEmailSent(true);
        confirmationReservationService.save(cr);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void removeFromDataBaseExpiredConfirmationReservations() {
        if (isEnabled) {
            confirmationReservationService.findAll().stream()
                    .filter(cr -> cr.getExpirationTime().isBefore(LocalDateTime.now().plusHours(2)))
                    .forEach(cr -> timer.schedule(getNewTimerTaskForRemoveExpiredConfirmationKeys(cr.getReservation()),
                            CustomDateUtil.toDate(cr.getExpirationTime().plusMinutes(1))));
        }
    }

    private TimerTask getNewTimerTaskForRemoveExpiredConfirmationKeys(Reservation reservation) {

        return new TimerTask() {
            @Override
            public void run() {
                if (isEnabled) {
                    if (!reservation.getConfirmed()) {
                        reservationService.deleteById(reservation.getId());
                    }
                }
            }
        };

    }
}

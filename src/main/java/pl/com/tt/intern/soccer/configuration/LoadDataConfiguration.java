package pl.com.tt.intern.soccer.configuration;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class LoadDataConfiguration {

    private final ConfirmationReservationService confirmationReservationService;
    private final Timer timer;
    private final ReservationService reservationService;
    private final ChangeAccountMailFactory mailFactory;
    private final ChangeAccountUrlGeneratorFactory urlFactory;

    @EventListener(ApplicationReadyEvent.class)
    public void addConfirmationsToList() {
        confirmationReservationService.findAllByEmailSend(false).stream()
                .filter(cr -> cr.getTimeToMailSend().isAfter(LocalDateTime.now()))
                .forEach(cr -> timer.schedule(getNewTimerTask(cr), DateUtil.toDate(cr.getTimeToMailSend())));
    }

    private TimerTask getNewTimerTask(ConfirmationReservation cr) {
        return new TimerTask() {
            @Override
            public void run() {

                String email = cr.getReservation().getUser().getEmail();

                String url = urlFactory.getUrlGenerator(AccountChangeType.CONFIRM_RESERVATION)
                        .generate(email, cr.getUuid());

                mailFactory.getMailSender(AccountChangeType.CONFIRM_RESERVATION)
                        .send(email, url);
                setMailSent(cr);
            }
        };
    }

    private void setMailSent(ConfirmationReservation cr) {
        cr.setEmailSent(true);
        confirmationReservationService.save(cr);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void removeFromDataBaseExpiredConfirmationReservations() {
        confirmationReservationService.findAll().stream()
                .filter(cr -> cr.getExpirationTime().isBefore(LocalDateTime.now().plusHours(2)))
                .forEach(cr -> timer.schedule(getNewTimerTaskForRemoveExpiredConfirmationKeys(cr.getReservation()), DateUtil.toDate(cr.getExpirationTime().plusMinutes(1))));
    }

    private TimerTask getNewTimerTaskForRemoveExpiredConfirmationKeys(Reservation reservation) {
        return new TimerTask() {
            @Override
            public void run() {
                if (!reservation.getConfirmed()) {
                    reservationService.deleteById(reservation.getId());
                }
            }
        };
    }

}

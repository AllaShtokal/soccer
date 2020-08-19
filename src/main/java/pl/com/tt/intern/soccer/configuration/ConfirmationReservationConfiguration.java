package pl.com.tt.intern.soccer.configuration;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

@Configuration
@AllArgsConstructor
@Slf4j
public class ConfirmationReservationConfiguration {

    private final ConfirmationReservationService service;
    private final Timer timer;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeListToConfirmationMail(){
        List<ConfirmationReservation> confirmationReservationList = service.findAllByEmailSend(false).stream()
                .filter(cr -> cr.getReservation().getDateFrom().isAfter(now()))
                .collect(toList());

        List<LocalDateTime> dateTimeToMailSentList = confirmationReservationList.stream()
                .map(ConfirmationReservation::getTimeToMailSend)
                .collect(toList());

        dateTimeToMailSentList.forEach(time -> {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                   log.debug("time: " + time);
                }
            }, DateUtil.toDate(time));
        });
    }
}

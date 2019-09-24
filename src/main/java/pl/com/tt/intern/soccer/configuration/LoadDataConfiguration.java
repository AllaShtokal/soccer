package pl.com.tt.intern.soccer.configuration;

import lombok.AllArgsConstructor;
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
import java.util.stream.Collectors;

@Configuration
@AllArgsConstructor
public class LoadDataConfiguration {

    private final ConfirmationReservationService service;
    private final Timer timer;

    @EventListener(ApplicationReadyEvent.class)
    public void addConfirmationsToList(){
        List<ConfirmationReservation> listOfAllConfirmationFromBase = service.findAllByEmailSend(false);
        List<ConfirmationReservation> filteredListOfConfirmationFromDatabase = listOfAllConfirmationFromBase.stream()
                .filter(cr -> cr.getTimeToMailSend().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        filteredListOfConfirmationFromDatabase
                .forEach(cr -> timer.schedule(getNewTimerTask(cr), DateUtil.toDate(cr.getTimeToMailSend())));

    }

    private TimerTask getNewTimerTask(ConfirmationReservation cr){
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("Wyślij maila");
                setMailSent(cr);
            }
        };
    }

    private void setMailSent(ConfirmationReservation cr){
        cr.setEmailSent(true);
        service.save(cr);
    }

}

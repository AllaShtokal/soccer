package pl.com.tt.intern.soccer.configuration;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.event.EventListener;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class AppConfiguration {

    private final ConfirmationReservationService service;

    @Value("${server.default.timezone}")
    private String defaultTimeZone;

    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimeZone));
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Timer timer(){ return new Timer();}

    @EventListener(ApplicationReadyEvent.class)
    public void initializeListToConfirmationMail(){
        List<ConfirmationReservation> confirmationReservationList = service.findAllByEmailSent(false).stream()
                .filter(cr -> now().isBefore(cr.getReservation().getDateFrom()))
                .collect(toList());

        List<LocalDateTime> dateTimeToMailSentList = confirmationReservationList.stream()
                .map(ConfirmationReservation::getTimeToSend)
                .collect(toList());

        Timer timer = timer();

        dateTimeToMailSentList.forEach(time -> {

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println(time);
                }
            },DateUtil.toDate(time));
        });
    }
}

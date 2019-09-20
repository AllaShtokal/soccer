package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.mail.MailSender;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.repository.ConfirmationReservationRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.util.DateUtil;
import pl.com.tt.intern.soccer.util.files.FileToString;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConfirmationReservationServiceImpl implements ConfirmationReservationService {

    private final ConfirmationReservationRepository repository;
    private final Timer timer;
    private final ConfirmationKeyService confirmationKeyService;
    private final MailSender mailSender;


    @Value("${docs.path.mail.confirm}")
    private String fileActiveMailMsg;

    @Value("${reservation.confirm.link}")
    private String activationLink;

    @Value("${reservation.confirm.mail.subject}")
    private String subjectActivationLink;

    @Override
    public ConfirmationReservation save(ConfirmationReservation confirmationReservation) {
        return repository.save(confirmationReservation);
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
    public void saveAndAddConfirmationReservationToTaskTimer(ConfirmationReservation confirmationReservation) {
        ConfirmationReservation cr = save(confirmationReservation);
        addTaskToMailSender(cr);
    }

    private void addTaskToMailSender(ConfirmationReservation confirmationReservation) {
        timer.schedule(getTimerTask(confirmationReservation), DateUtil.toDate(confirmationReservation.getTimeToSend()));
    }

    private TimerTask getTimerTask(ConfirmationReservation confirmationReservation) {
        return new TimerTask() {
            @Override
            public void run() {
                sendActiveTokenMailMsg(confirmationReservation);
            }
        };
    }

    private void sendActiveTokenMailMsg(ConfirmationReservation confirmationReservation) {
        User user = confirmationReservation.getReservation().getUser();
        ConfirmationKey confirmationKey = new ConfirmationKey(user);
        confirmationKeyService.save(confirmationKey);

        try {
            String msg = FileToString.readFileToString(fileActiveMailMsg);
            String msgMail = insertActivationLinkToMailMsg(msg, confirmationKey);

            mailSender.sendSimpleMessageHtml(
                    user.getEmail(),
                    subjectActivationLink,
                    msgMail
            );

            confirmationReservation.setEmailSent(true);
            save(confirmationReservation);

        } catch (IOException e) {
            log.error("Throwing an IOException while reading the file.. ", e);
        }
    }

    private String insertActivationLinkToMailMsg(String msg, ConfirmationKey confirmationKey) {
        StringBuilder newString = new StringBuilder(msg);

        return newString.insert(
                msg.indexOf("\">Potwierdź rezerwację</a>"),
                activationLink + confirmationKey.getUuid()
        ).toString();
    }
}

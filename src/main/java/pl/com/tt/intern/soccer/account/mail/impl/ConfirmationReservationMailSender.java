package pl.com.tt.intern.soccer.account.mail.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;
import pl.com.tt.intern.soccer.mail.customizer.impl.CustomizedSenderImpl;

import java.util.Locale;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.CONFIRM_RESERVATION;

@Service
@RequiredArgsConstructor
public class ConfirmationReservationMailSender implements PerAccountTypeMailSender {

    private final String FILE_NAME = "confirmation_reservation";
    private final MessageSource messageSource;
    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        String subject = messageSource.getMessage("account.reservation.confirm.mail.subject", null, Locale.US);
        sender.insertLinkToMsgAndSendMail(email, FILE_NAME, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return CONFIRM_RESERVATION.equals(type);
    }
}

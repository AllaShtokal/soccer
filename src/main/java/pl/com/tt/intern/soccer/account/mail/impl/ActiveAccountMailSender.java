package pl.com.tt.intern.soccer.account.mail.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;
import pl.com.tt.intern.soccer.mail.customizer.impl.CustomizedSenderImpl;

import java.util.Locale;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.ACTIVE_ACCOUNT;

@Service
@RequiredArgsConstructor
public class ActiveAccountMailSender implements PerAccountTypeMailSender {

    private final String FILE_NAME = "active";
    private final MessageSource messageSource;
    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        String subject = messageSource.getMessage("account.active.mail.subject", null, Locale.US);
        sender.insertLinkToMsgAndSendMail(email, FILE_NAME, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return ACTIVE_ACCOUNT.equals(type);
    }
}

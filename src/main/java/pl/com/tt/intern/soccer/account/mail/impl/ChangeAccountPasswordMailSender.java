package pl.com.tt.intern.soccer.account.mail.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;
import pl.com.tt.intern.soccer.mail.customizer.impl.CustomizedSenderImpl;

import java.util.Locale;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;

@Service
@RequiredArgsConstructor
public class ChangeAccountPasswordMailSender implements PerAccountTypeMailSender {

    private final String FILE_NAME = "change_password";
    private final MessageSource messageSource;
    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        String subject = messageSource.getMessage("account.change.password.mail.subject", null, Locale.US);
        sender.insertLinkToMsgAndSendMail(email, FILE_NAME, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return NOT_LOGGED_IN_USER_PASSWORD.equals(type);
    }
}

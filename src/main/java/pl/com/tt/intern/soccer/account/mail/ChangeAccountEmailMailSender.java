package pl.com.tt.intern.soccer.account.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.mail.customizer.CustomizedSenderImpl;

import java.util.Locale;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Service
@RequiredArgsConstructor
public class ChangeAccountEmailMailSender implements PerAccountTypeMailSender {

    private final String FILE_NAME = "change_email";
    private final MessageSource messageSource;
    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        String subject = messageSource.getMessage("account.change.email.mail.subject", null, Locale.US);
        sender.insertLinkToMsgAndSendMail(email, FILE_NAME, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return type.equals(EMAIL);
    }
}

package pl.com.tt.intern.soccer.account.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.mail.customizer.CustomizedSenderImpl;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.*;

@Service
@RequiredArgsConstructor
public class ChangeAccountEmailMailSender implements PerAccountTypeMailSender {

    @Value("${properties.account.change.email.file-name}")
    private String fileName;

    @Value("${properties.account.change.email.mail.subject}")
    private String subject;

    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        sender.insertLinkToMsgAndSendMail(email, fileName, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return type.equals(EMAIL);
    }
}

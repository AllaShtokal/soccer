package pl.com.tt.intern.soccer.account.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.mail.customizer.CustomizedSenderImpl;

@Service
@RequiredArgsConstructor
public class ChangeAccountPasswordMailSender implements PerAccountTypeMailSender {

    @Value("${properties.account.change.password.file-name}")
    private String fileName;

    @Value("${properties.account.change.password.mail.subject}")
    private String subject;

    private final CustomizedSenderImpl sender;

    @Override
    public void send(String email, String url) {
        sender.insertLinkToMsgAndSendMail(email, fileName, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return type.equals(AccountChangeType.valueOf(201));
    }
}

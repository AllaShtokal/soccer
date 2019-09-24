package pl.com.tt.intern.soccer.account.mail;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.mail.customizer.CustomizedSender;

@Service
@RequiredArgsConstructor
public class ActiveAccountMailSender implements PerAccountTypeMailSender {

    @Value("${properties.account.active.file-name}")
    private String fileName;

    @Value("${properties.account.active.mail.subject}")
    private String subject;

    private final CustomizedSender sender;

    @Override
    public void send(String email, String url) {
        sender.insertLinkToMsgAndSendMail(email, fileName, subject, url);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return type.equals(AccountChangeType.valueOf(202));
    }
}
package pl.com.tt.intern.soccer.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {

    @Value("${mail.config.address}")
    private String emailFrom;
    private final JavaMailSender javaMailSender;

    @Override
    public void sendSimpleMessageText(String to, String subject, String text) {
        log.debug("Sending simple email without html, to: {}. subject: {}", to, subject);
        sendMessage(to, subject, text, false);
    }

    @Override
    public void sendSimpleMessageHtml(String to, String subject, String text) {
        log.debug("Sending simple email with html, to: {}. subject: {}", to, subject);
        sendMessage(to, subject, text, true);
    }

    private void sendMessage(String to, String subject, String text, boolean html) {
        log.debug("Sending email, to: {}. subject: {}, type text: {}", to, subject, html);
        MimeMessage msg = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "utf-8");
            setBasicMessageValues(helper, to, subject, text, html);
        } catch (MessagingException e) {
            log.error("Sending simple email failed.. ", e);
        }

        javaMailSender.send(msg);

    }

    private void setBasicMessageValues(MimeMessageHelper helper, String to, String subject, String text, boolean html) throws MessagingException {
        helper.setTo(to);
        helper.setFrom(emailFrom);
        helper.setSubject(subject);
        helper.setText(text, html);
    }

}

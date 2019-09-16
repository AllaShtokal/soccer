package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.mail.MailSender;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.SendMailService;
import pl.com.tt.intern.soccer.util.files.FileToString;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final ConfirmationKeyService confirmationKeyService;
    private final MailSender mailSender;

    public void sendEmailWithMessageFromFileAndInsertLinkWithToken(User user, String fileName, String subject, String linkToInsert, String indexOfByText) {
        ConfirmationKey confirmationKey = new ConfirmationKey(user);
        confirmationKeyService.save(confirmationKey);

        try {
            String msg = FileToString.readFileToString(fileName);
            String msgMail = insertLinkWithTokenToMailMsg(confirmationKey, msg, linkToInsert, indexOfByText);
            mailSender.sendSimpleMessageHtml(
                    user.getEmail(),
                    subject,
                    msgMail
            );
        } catch (IOException e) {
            log.error("Throwing an IOException while reading the file.. ", e);
        }
    }

    private String insertLinkWithTokenToMailMsg(ConfirmationKey confirmationKey, String msg, String link, String indexOfByText) {
        StringBuilder newString = new StringBuilder(msg);
        return newString.insert(
                msg.indexOf(indexOfByText),
                link + confirmationKey.getUuid()
        ).toString();
    }
}

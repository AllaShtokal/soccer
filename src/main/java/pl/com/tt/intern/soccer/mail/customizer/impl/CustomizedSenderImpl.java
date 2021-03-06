package pl.com.tt.intern.soccer.mail.customizer.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import pl.com.tt.intern.soccer.mail.MailSender;
import pl.com.tt.intern.soccer.mail.customizer.CustomizedSender;
import pl.com.tt.intern.soccer.mail.customizer.MailCustomizerTemplate;

@Component
@RequiredArgsConstructor
public class CustomizedSenderImpl implements CustomizedSender {

    @Qualifier("springTemplateEngine")
    private final SpringTemplateEngine templateEngine;
    private final MailSender mailSender;

    @Override
    public void insertLinkToMsgAndSendMail(String emailTo, String fileName, String subject, String linkToInsert) {
        MailCustomizerTemplate template = new MailCustomizerTemplate(linkToInsert);
        Context context = new Context();

        context.setVariable("emailTemplate", template);
        String msgMail = templateEngine.process(fileName, context);
        mailSender.sendSimpleMessageHtml(
                emailTo,
                subject,
                msgMail
        );
    }
}

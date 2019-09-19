package pl.com.tt.intern.soccer.account.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.com.tt.intern.soccer.account.mail.MailSenderAccount;
import pl.com.tt.intern.soccer.account.url.ChangeAccountUrlGenerator;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChangeAccountFactory implements ChangeAccount {

    private final List<ChangeAccountUrlGenerator> urlGenerators;
    private final List<MailSenderAccount> mailSendersAccount;

    @Override
    public ChangeAccountUrlGenerator getUrlGenerator(AccountChangeType type) {
        return urlGenerators.stream()
                .filter(customizer -> customizer.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Don't find url generators."));
    }

    @Override
    public MailSenderAccount getMailSender(AccountChangeType type) {
        return mailSendersAccount.stream()
                .filter(customizer -> customizer.supports(type))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Don't find mail senders account."));
    }
}

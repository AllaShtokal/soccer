package pl.com.tt.intern.soccer.account.factory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChangeAccountMailFactoryImpl implements ChangeAccountMailFactory {

    private final List<PerAccountTypeMailSender> mailSendersAccount;

    @Override
    public PerAccountTypeMailSender getMailSender(AccountChangeType type) {
        return mailSendersAccount.stream()
                .filter(customizer -> customizer.supports(type))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalStateException("Cannot find PerAccountTypeMailSender for type: " + type));
    }
}

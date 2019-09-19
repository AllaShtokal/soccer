package pl.com.tt.intern.soccer.account.factory;

import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;
import pl.com.tt.intern.soccer.account.url.ChangeAccountUrlGenerator;

public interface ChangeAccount {
    ChangeAccountUrlGenerator getUrlGenerator(AccountChangeType type);

    PerAccountTypeMailSender getMailSender(AccountChangeType type);
}

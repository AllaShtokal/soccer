package pl.com.tt.intern.soccer.account.factory;

import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender;

public interface ChangeAccountMailFactory {
    PerAccountTypeMailSender getMailSender(AccountChangeType type);
}

package pl.com.tt.intern.soccer.account.factory;

import pl.com.tt.intern.soccer.account.mail.MailSenderAccount;
import pl.com.tt.intern.soccer.account.url.ChangeAccountUrlGenerator;

public interface ChangeAccount {
    ChangeAccountUrlGenerator getUrlGenerator(AccountChangeType type);

    MailSenderAccount getMailSender(AccountChangeType type);
}

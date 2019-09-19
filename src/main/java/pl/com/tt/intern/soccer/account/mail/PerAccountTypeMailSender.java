package pl.com.tt.intern.soccer.account.mail;

import pl.com.tt.intern.soccer.account.factory.AccountChangeType;

public interface PerAccountTypeMailSender {
    void send(String email, String url);

    boolean supports(AccountChangeType type);
}

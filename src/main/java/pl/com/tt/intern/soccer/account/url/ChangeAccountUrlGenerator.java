package pl.com.tt.intern.soccer.account.url;

import pl.com.tt.intern.soccer.account.factory.AccountChangeType;

public interface ChangeAccountUrlGenerator {
    String generate(String mail, String... params);

    boolean supports(AccountChangeType type);
}

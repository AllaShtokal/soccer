package pl.com.tt.intern.soccer.account.factory;

import pl.com.tt.intern.soccer.account.url.AccountChangeUrlGenerator;

public interface ChangeAccountUrlGeneratorFactory {
    AccountChangeUrlGenerator getUrlGenerator(AccountChangeType type);
}

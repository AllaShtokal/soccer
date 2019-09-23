package pl.com.tt.intern.soccer.account.factory;

import pl.com.tt.intern.soccer.account.url.ChangeAccountUrlGenerator;

public interface ChangeAccountUrlGeneratorFactory {
    ChangeAccountUrlGenerator getUrlGenerator(AccountChangeType type);
}

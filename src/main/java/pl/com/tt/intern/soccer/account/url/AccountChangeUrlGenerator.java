package pl.com.tt.intern.soccer.account.url;

import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;

import java.util.Map;

public interface AccountChangeUrlGenerator {
    String generate(Map<UrlParam, String> params);

    boolean supports(AccountChangeType type);
}

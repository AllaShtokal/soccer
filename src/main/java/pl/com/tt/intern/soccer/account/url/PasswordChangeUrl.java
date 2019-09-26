package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.account.url.util.UrlGeneratorHelper;

import java.util.Map;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordChangeUrl implements AccountChangeUrlGenerator {

    private final String URL_SUFFIX = "change-password";

    @SneakyThrows
    @Override
    public String generate(Map<UrlParam, String> params) {
        return UrlGeneratorHelper.createUrl(URL_SUFFIX, params);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return NOT_LOGGED_IN_USER_PASSWORD.equals(type);
    }

}

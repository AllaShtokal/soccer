package pl.com.tt.intern.soccer.account.url.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AbstractAccountChangeUrlGenerator;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordChangeUrlGenerator extends AbstractAccountChangeUrlGenerator {

    @Getter
    private final String urlSuffix = "login/password-change";

    @Override
    public boolean supports(AccountChangeType type) {
        return NOT_LOGGED_IN_USER_PASSWORD.equals(type);
    }

}

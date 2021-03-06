package pl.com.tt.intern.soccer.account.url.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AbstractAccountChangeUrlGenerator;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.ACTIVE_ACCOUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountActivationUrlGenerator extends AbstractAccountChangeUrlGenerator {

    @Getter
    private static final String URL_SUFFIX = "login";

    @Override
    public boolean supports(AccountChangeType type) {
        return ACTIVE_ACCOUNT.equals(type);
    }

    @Override
    public String getUrlSuffix() {
        return null;
    }
}

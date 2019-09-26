package pl.com.tt.intern.soccer.account.url.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AbstractAccountChangeUrlGenerator;
import pl.com.tt.intern.soccer.account.url.AccountChangeUrlGenerator;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.ACTIVE_ACCOUNT;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountActivationUrlGenerator extends AbstractAccountChangeUrlGenerator implements AccountChangeUrlGenerator {

    @Getter
    private final String urlSuffix = "login";

    @Override
    public boolean supports(AccountChangeType type) {
        return ACTIVE_ACCOUNT.equals(type);
    }

}

package pl.com.tt.intern.soccer.account.url.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AbstractAccountChangeUrlGenerator;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChangeUrlGenerator extends AbstractAccountChangeUrlGenerator {

    @Getter
    private final String urlSuffix = "user-settings";

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }
}

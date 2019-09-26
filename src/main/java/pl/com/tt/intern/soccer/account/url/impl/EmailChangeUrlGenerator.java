package pl.com.tt.intern.soccer.account.url.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AccountChangeUrlGenerator;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.account.url.util.UrlGeneratorHelper;

import java.util.Map;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailChangeUrlGenerator implements AccountChangeUrlGenerator {

    private final String urlSuffix = "user-settings";

    @SneakyThrows
    @Override
    public String generate(Map<UrlParam, String> params) {
        return UrlGeneratorHelper.createUrl(urlSuffix, params);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }
}

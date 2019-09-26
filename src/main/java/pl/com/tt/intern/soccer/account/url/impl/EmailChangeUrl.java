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
public class EmailChangeUrl implements AccountChangeUrlGenerator {

    private final String URL_SUFFIX = "user-settings";

    @SneakyThrows
    @Override
    public String generate(Map<UrlParam, String> params) {
        return UrlGeneratorHelper.createUrl(URL_SUFFIX, params);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }
}

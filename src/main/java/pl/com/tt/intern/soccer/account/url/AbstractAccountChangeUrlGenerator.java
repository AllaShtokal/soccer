package pl.com.tt.intern.soccer.account.url;

import lombok.SneakyThrows;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.account.url.util.UrlGeneratorHelper;

import java.util.Map;

public abstract class AbstractAccountChangeUrlGenerator implements AccountChangeUrlGenerator {
    public abstract String getUrlSuffix();

    @SneakyThrows
    public String generate(Map<UrlParam, String> params) {
        return UrlGeneratorHelper.createUrl(getUrlSuffix(), params);
    }
}

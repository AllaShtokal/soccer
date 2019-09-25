package pl.com.tt.intern.soccer.account.url.util;

import lombok.NoArgsConstructor;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class UrlParameterGeneratorUtil {

    public static String generate(String baseUrl, Map<String, String> map) {
        StringBuilder url = new StringBuilder(baseUrl).append("?");

        map.forEach(
                (key, value) -> url
                        .append(key)
                        .append("=")
                        .append(value)
                        .append("&")
        );

        return url.toString();
    }
}

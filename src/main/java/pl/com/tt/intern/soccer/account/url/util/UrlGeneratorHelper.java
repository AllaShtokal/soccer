package pl.com.tt.intern.soccer.account.url.util;

import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;

import java.util.Map;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class UrlGeneratorHelper {

    public static String createUrl(String urlSuffix, Map<UrlParam, String> map) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        map.forEach((k, v) -> params.add(k.getParameterName(), v));

        return UriComponentsBuilder.newInstance()
                .host(HostGeneratorUtil.generate())
                .path(urlSuffix)
                .queryParams(params)
                .build()
                .toString();
    }
}

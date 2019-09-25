package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.util.UrlParameterGeneratorUtil;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeEmailUrl implements ChangeAccountUrlGenerator {

    private final String LINK_MAIN = "/change-data";
    private final String LINK_PARAM_FIRST = "newEmail";
    private final String LINK_PARAM_SECOND = "changeEmailKey";
    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;

    @Value("${frontend.server.address}")
    private String serverAddress;

    @Value("${frontend.server.port}")
    private String serverPort;

    @SneakyThrows
    @Override
    public String generate(String mail, String... params) {
        ConfirmationKey confirmationKey = new ConfirmationKey(
                userService.findByEmail(mail)
        );
        confirmationKeyService.save(confirmationKey);

        return createUrl(
                confirmationKey.getUuid(),
                params
        );
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }

    private String createUrl(String uuid, String... params) {
        String baseUrl = serverAddress + ":" + serverPort + LINK_MAIN;
        Map<String, String> urlParams = new HashMap<>();

        urlParams.put(LINK_PARAM_FIRST, params[0]);
        urlParams.put(LINK_PARAM_SECOND, uuid);
        return UrlParameterGeneratorUtil.generate(baseUrl, urlParams);
    }
}

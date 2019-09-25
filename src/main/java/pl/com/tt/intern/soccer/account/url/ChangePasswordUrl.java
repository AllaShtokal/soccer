package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.util.HostGeneratorUtil;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordUrl implements ChangeAccountUrlGenerator {

    private final String URL_SUFFIX = "change-password";
    private final String CHANGE_PASSWORD_KEY_PARAM = "changePasswordKey";
    private final ConfirmationKeyService confirmationKeyService;

    @SneakyThrows
    @Override
    public String generate(String email, String... params) {
        String uuid = confirmationKeyService.createAndAssignToUserByEmail(email).getUuid();
        return createUrl(uuid);
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return NOT_LOGGED_IN_USER_PASSWORD.equals(type);
    }

    private String createUrl(String uuid) {
        return UriComponentsBuilder.newInstance()
                .host(HostGeneratorUtil.generate())
                .path(URL_SUFFIX)
                .queryParam(CHANGE_PASSWORD_KEY_PARAM, uuid)
                .build()
                .toString();
    }
}

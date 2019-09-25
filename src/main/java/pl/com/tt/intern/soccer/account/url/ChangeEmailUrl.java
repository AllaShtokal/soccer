package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.util.HostGeneratorUtil;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeEmailUrl implements ChangeAccountUrlGenerator {

    private final String URL_SUFFIX = "/change-data";
    private final String NEW_EMAIL_PARAM = "newEmail";
    private final String CHANGE_EMAIL_KEY_PARAM = "changeEmailKey";
    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;

    @SneakyThrows
    @Override
    public String generate(String mail, String... params) {
        ConfirmationKey confirmationKey = new ConfirmationKey(
                userService.findByEmail(mail)
        );
        confirmationKeyService.save(confirmationKey);

        return createUrl(
                confirmationKey.getUuid(),
                params[0]
        );
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }

    private String createUrl(String uuid, String email) {
        return UriComponentsBuilder.newInstance()
                .host(HostGeneratorUtil.generate())
                .path(URL_SUFFIX)
                .queryParam(NEW_EMAIL_PARAM, email)
                .queryParam(CHANGE_EMAIL_KEY_PARAM, uuid)
                .build()
                .toString();
    }
}

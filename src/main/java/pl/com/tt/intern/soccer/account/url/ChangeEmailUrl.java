package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangeEmailUrl implements ChangeAccountUrlGenerator {

    @Value("${properties.account.change.email.link.main}")
    private String link;

    @Value("${properties.account.change.email.link.param1}")
    private String linkParamFirst;

    @Value("${properties.account.change.email.link.param2}")
    private String linkParamSecond;

    @Value("${frontend.server.address}")
    private String serverAddress;

    @Value("${frontend.server.port}")
    private String serverPort;

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;

    @Override
    public String generate(String mail, String... params) {
        try {

            ConfirmationKey confirmationKey = new ConfirmationKey(
                    userService.findByEmail(mail)
            );
            confirmationKeyService.save(confirmationKey);

            return createUrl(
                    confirmationKey.getUuid(),
                    params
            );
        } catch (NotFoundException e) {
            log.error("Not found user..", e);
            return null;
        }
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return EMAIL.equals(type);
    }

    private String createUrl(String uuid, String... params) {
        return String.format("%s:%s/%s?%s=%s&%s=%s",
                serverAddress,
                serverPort,
                link,
                linkParamFirst,
                params[0],
                linkParamSecond,
                uuid);
    }
}

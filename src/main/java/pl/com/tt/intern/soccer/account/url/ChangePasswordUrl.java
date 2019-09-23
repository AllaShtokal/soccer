package pl.com.tt.intern.soccer.account.url;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordUrl implements ChangeAccountUrlGenerator {

    @Value("${properties.account.change.password.link}")
    private String link;

    @Value("${frontend.server.address}")
    private String serverAddress;

    @Value("${frontend.server.port}")
    private String serverPort;

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;

    @Override
    public String generate(String email, String... params) {
        try {
            User user = userService.findByEmail(email);
            ConfirmationKey confirmationKey = new ConfirmationKey(user);

            confirmationKeyService.save(confirmationKey);
            return serverAddress + ":" + serverPort + link + confirmationKey.getUuid();
        } catch (NotFoundException e) {
            log.error("Not found email. ", e);
            return null;
        }
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return NOT_LOGGED_IN_USER_PASSWORD.equals(type);
    }

}

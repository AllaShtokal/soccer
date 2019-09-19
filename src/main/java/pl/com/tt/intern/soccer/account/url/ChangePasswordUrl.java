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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChangePasswordUrl implements ChangeAccountUrlGenerator {

    @Value("${docs.name.mail.change.password}")
    private String fileName;

    @Value("${account.change.password.link}")
    private String link;

    @Value("${account.change.password.mail.subject}")
    private String subject;

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
        return type.equals(AccountChangeType.valueOf(201));
    }

}

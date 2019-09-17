package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.mail.MailCustomizer;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.request.PasswordChangerRequest;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Value("${docs.path.mail.change.password}")
    private String fileChangePasswordMailMsg;

    @Value("${account.change.password.link}")
    private String changePasswordLink;

    @Value("${account.change.password.mail.subject}")
    private String changePasswordSubject;

    @Value("${account.change.password.indexOfByText}")
    private String indexOfByTextChangePassword;

    @Value("${frontend.server.address}")
    private String serverAddress;

    @Value("${frontend.server.port}")
    private String serverPort;

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;
    private final MailCustomizer sendMailService;

    @Override
    public void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activationKey);

            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new IncorrectConfirmationKeyException("The account activation token can't be found in the database.");
        }
    }

    @Override
    public void sendMailToChangePassword(String email) throws NotFoundException {
        String url = serverAddress + ":" + serverPort + changePasswordLink;
        User user = userService.findByEmail(email);
        ConfirmationKey confirmationKey = new ConfirmationKey(user);

        confirmationKeyService.save(confirmationKey);
        sendMailService.sendEmailWithMessageFromFileAndInsertLinkWithToken(
                confirmationKey,
                fileChangePasswordMailMsg,
                changePasswordSubject,
                url,
                indexOfByTextChangePassword);
    }

    @Override
    public void changePassword(String changePasswordKey, PasswordChangerRequest request) throws Exception {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey);
            checkIfExpired(confirmationKey.getExpirationTime());

            if (request.getPassword().equals(request.getConfirmPassword())) {
                User user = confirmationKey.getUser();
                user.setPassword(request.getPassword());
                confirmationKey.setExpirationTime(now());
                userService.changePassword(user);
            } else
                throw new PasswordsMismatchException();
        } catch (NotFoundException e) {
            throw new IncorrectConfirmationKeyException("The token can't be found in the database.");
        }
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws IncorrectConfirmationKeyException {
        if (!expirationTimeToken.isAfter(now()))
            throw new IncorrectConfirmationKeyException("The token has expired.");
    }
}

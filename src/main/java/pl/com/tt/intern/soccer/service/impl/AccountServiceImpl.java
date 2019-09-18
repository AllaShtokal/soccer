package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.mail.MailCustomizer;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.security.UserPrincipal;
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
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;

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
    public void changePasswordNotLoggedUser(String changePasswordKey, ForgottenPasswordRequest request) throws Exception {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey);
            checkIfExpired(confirmationKey.getExpirationTime());

            if (request.getPassword().equals(request.getPasswordConfirmation())) {
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

    @Override
    public void deactivate(Long userId) throws NotFoundException {
        User user = userService.findById(userId);
        userService.changeEnabledAccount(user, false);
    }

    @Override
    public void changePasswordLoggedUser(UserPrincipal userPrincipal, ChangePasswordRequest request) throws InvalidChangePasswordException {
        if (passwordChangePossible(request, userPrincipal.getPassword())) {
            User user = mapper.map(userPrincipal, User.class);
            user.setPassword(request.getNewPassword());
            userService.changePassword(user);
        } else throw new InvalidChangePasswordException("Incorrect old password or new passwords do not match.");
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws IncorrectConfirmationKeyException {
        if (!expirationTimeToken.isAfter(now()))
            throw new IncorrectConfirmationKeyException("The token has expired.");
    }

    private boolean passwordChangePossible(ChangePasswordRequest request, String oldPassword) {
        boolean matchesNewPassword = request.getNewPassword().equals(request.getNewPasswordConfirmation());
        boolean matchesOldPassword = encoder.matches(request.getOldPassword(), oldPassword);
        return matchesOldPassword && matchesNewPassword;
    }
}

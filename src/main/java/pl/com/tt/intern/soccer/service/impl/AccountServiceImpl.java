package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.SendMailService;
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

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;
    private final SendMailService sendMailService;

    @Override
    public void activateAccountByToken(String activeToken) throws ActivationAccountException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activeToken);
            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new ActivationAccountException("The account activation token can't be found in the database.");
        }
    }

    @Override
    public void sendMailToChangePassword(String email) throws NotFoundException {
        User user = userService.findByEmail(email);
        sendMailService.sendEmailWithMessageFromFileAndInsertLinkWithToken(
                user, fileChangePasswordMailMsg, changePasswordSubject, changePasswordLink, indexOfByTextChangePassword);
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws ActivationAccountException {
        if (!expirationTimeToken.isAfter(now()))
            throw new ActivationAccountException("The account activation token has expired.");
    }
}

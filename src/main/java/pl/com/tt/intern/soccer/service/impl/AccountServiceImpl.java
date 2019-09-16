package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.CorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
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
    private final ModelMapper mapper;

    @Override
    public void activateAccountByToken(String activeToken) throws CorrectTokenException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activeToken);
            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new CorrectTokenException("The account activation token can't be found in the database.");
        }
    }

    @Override
    public void sendMailToChangePassword(String email) throws NotFoundException {
        User user = userService.findByEmail(email);
        sendMailService.sendEmailWithMessageFromFileAndInsertLinkWithToken(
                user, fileChangePasswordMailMsg, changePasswordSubject, changePasswordLink, indexOfByTextChangePassword);
    }

    @Override
    public void changePassword(String changePasswordToken, ChangePasswordRequest request) throws Exception {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(changePasswordToken);
            checkIfExpired(confirmationKey.getExpirationTime());
            if (request.getPassword().equals(request.getConfirmPassword())) {
                User user = confirmationKey.getUser();
                user.setPassword(request.getPassword());
                confirmationKey.setExpirationTime(now());
                userService.changePassword(user);
            } else
                throw new PasswordsMismatchException();
        } catch (NotFoundException e) {
            throw new CorrectTokenException("The token can't be found in the database.");
        }
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws CorrectTokenException {
        if (!expirationTimeToken.isAfter(now()))
            throw new CorrectTokenException("The token has expired.");
    }
}

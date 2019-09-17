package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.IncorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.mail.MailCustomizer;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.account.ChangePassword;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
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

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;
    private final MailCustomizer sendMailService;
    private final ModelMapper mapper;

    @Override
    public void activateAccountByToken(String activeConfirmKey) throws IncorrectTokenException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activeConfirmKey);

            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new IncorrectTokenException("The account activation token can't be found in the database.");
        }
    }

    @Override
    public void sendMailToChangePassword(String email) throws NotFoundException {
        User user = userService.findByEmail(email);
        ConfirmationKey confirmationKey = new ConfirmationKey(user);

        confirmationKeyService.save(confirmationKey);
        sendMailService.sendEmailWithMessageFromFileAndInsertLinkWithToken(
                confirmationKey,
                fileChangePasswordMailMsg,
                changePasswordSubject,
                changePasswordLink,
                indexOfByTextChangePassword);
    }

    @Override
    public void changePassword(String changePasswordConfirmKey, ChangePasswordRequest request) throws Exception {
        ChangePassword cp = mapper.map(request, ChangePassword.class);
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(changePasswordConfirmKey);
            checkIfExpired(confirmationKey.getExpirationTime());

            if (cp.getPassword().equals(cp.getConfirmPassword())) {
                User user = confirmationKey.getUser();
                user.setPassword(cp.getPassword());
                confirmationKey.setExpirationTime(now());
                userService.changePassword(user);
            } else
                throw new PasswordsMismatchException();
        } catch (NotFoundException e) {
            throw new IncorrectTokenException("The token can't be found in the database.");
        }
    }

    @Override
    public void deactivate(Long id) throws NotFoundException {
        User user = userService.findById(id);
        userService.changeEnabledAccount(user, false);
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws IncorrectTokenException {
        if (!expirationTimeToken.isAfter(now()))
            throw new IncorrectTokenException("The token has expired.");
    }
}

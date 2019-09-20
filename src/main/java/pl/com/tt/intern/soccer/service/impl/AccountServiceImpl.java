package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyForSignUpService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ConfirmationKeyForSignUpService confirmationKeyForSignUpService;
    private final UserService userService;

    @Override
    public void activateAccountByToken(String activeToken) throws ActivationAccountException {
        try {
            ConfirmationKeyForSignUp confirmationKeyForSignUp = confirmationKeyForSignUpService.findConfirmationKeyByUuid(activeToken);
            checkIfExpired(confirmationKeyForSignUp.getExpirationTime());
            confirmationKeyForSignUp.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKeyForSignUp.getUser(), true);
        } catch (NotFoundException e) {
            throw new ActivationAccountException("The account activation token can't be found in the database.");
        }
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws ActivationAccountException {
        if (!expirationTimeToken.isAfter(now()))
            throw new ActivationAccountException("The account activation token has expired.");
    }
}

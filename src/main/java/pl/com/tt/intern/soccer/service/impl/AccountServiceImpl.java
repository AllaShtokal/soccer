package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;

    @Override
    public void activateAccountByToken(String activeToken) throws ActivationAccountException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activeToken);
            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(LocalDateTime.now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new ActivationAccountException("The account activation token can't be found in the database.");
        }
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws ActivationAccountException {
        if (!expirationTimeToken.isAfter(LocalDateTime.now()))
            throw new ActivationAccountException("The account activation token has expired.");
    }
}

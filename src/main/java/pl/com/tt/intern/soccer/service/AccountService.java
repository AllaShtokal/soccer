package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.PasswordChangerRequest;

public interface AccountService {

    void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException;

    void sendMailToChangePassword(String email) throws NotFoundException;

    void changePassword(String changePasswordKey, PasswordChangerRequest request) throws Exception;

    void deactivate(Long userId) throws NotFoundException;
}


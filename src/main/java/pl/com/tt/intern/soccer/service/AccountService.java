package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.PasswordChangerRequest;

public interface AccountService {

    void activateAccountByToken(String activationKey) throws IncorrectTokenException;

    void sendMailToChangePassword(String email) throws NotFoundException;

    void changePassword(String changePasswordKey, PasswordChangerRequest request) throws Exception;
}


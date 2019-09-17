package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.PasswordChangerRequest;

public interface AccountService {

    void activateAccountByToken(String activeConfirmKey) throws IncorrectTokenException;

    void sendMailToChangePassword(String email) throws NotFoundException;

    void changePassword(String changePasswordConfirmKey, PasswordChangerRequest request) throws Exception;
}


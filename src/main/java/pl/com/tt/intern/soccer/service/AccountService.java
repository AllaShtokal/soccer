package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;

public interface AccountService {

    void activateAccountByToken(String activeConfirmKey) throws IncorrectTokenException;

    void sendMailToChangePassword(String email) throws NotFoundException;

    void changePassword(String changePasswordConfirmKey, ChangePasswordRequest request) throws Exception;

    void deactivate(Long userId) throws NotFoundException;
}


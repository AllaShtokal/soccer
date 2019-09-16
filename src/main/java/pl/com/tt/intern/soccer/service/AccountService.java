package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;

public interface AccountService {

    void activateAccountByToken(String activeToken) throws ActivationAccountException;

    void sendMailToChangePassword(String email) throws NotFoundException;
}


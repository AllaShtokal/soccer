package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.ActivationAccountException;

public interface AccountService {

    void activateAccountByToken(String activeToken) throws ActivationAccountException;

}


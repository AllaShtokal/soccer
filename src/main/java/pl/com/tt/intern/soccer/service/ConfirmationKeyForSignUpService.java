package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;

public interface ConfirmationKeyForSignUpService {

    ConfirmationKeyForSignUp save(ConfirmationKeyForSignUp token);

    ConfirmationKeyForSignUp findConfirmationKeyByUuid(String uuid) throws NotFoundException;

    void scanAndDeleteExpiredConfirmationKeys();

}

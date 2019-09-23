package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;

public interface AccountService {

    void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException;

    void setAndSendMailToChangePassword(String email);

    void changePasswordNotLoggedInUser(String changePasswordKey, ForgottenPasswordRequest request) throws Exception;

    void deactivate(Long userId) throws NotFoundException;

    ChangeDataAccountResponse changeUserInfo(UserPrincipal user, ChangeAccountDataRequest request) throws NotFoundException;
}


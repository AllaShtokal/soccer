package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.payload.request.EmailRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.security.UserPrincipal;

public interface AccountService {

    void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException;

    void setAndSendMailToChangePassword(String email);

    void setAndSendMailToChangeEmail(String email, String newEmail);

    void changePasswordNotLoggedInUser(String changePasswordKey, ForgottenPasswordRequest request) throws Exception;

    void deactivate(Long userId) throws NotFoundException;

    void changePasswordLoggedInUser(UserPrincipal user, ChangePasswordRequest request) throws InvalidChangePasswordException;

    void changeEmail(UserPrincipal user, String changeEmailKey, EmailRequest request) throws Exception;
}


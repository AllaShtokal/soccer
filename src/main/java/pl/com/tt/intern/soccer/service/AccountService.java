package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.payload.request.EmailRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse;
import pl.com.tt.intern.soccer.payload.response.EmailChangeKeyResponse;
import pl.com.tt.intern.soccer.payload.response.PasswordChangeKeyResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;

public interface AccountService {

    void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException;

    PasswordChangeKeyResponse setAndSendMailToChangePassword(String email);

    void changePasswordNotLoggedInUser(String changePasswordKey, ForgottenPasswordRequest request)
            throws PasswordsMismatchException, IncorrectConfirmationKeyException;

    EmailChangeKeyResponse setAndSendMailToChangeEmail(String email, String newEmail);

    void deactivate(Long userId) throws NotFoundException;

    void changePasswordLoggedInUser(UserPrincipal user, ChangePasswordRequest request) throws InvalidChangePasswordException;

    ChangeDataAccountResponse changeUserInfo(UserPrincipal user, ChangeAccountDataRequest request) throws NotFoundException;

    void changeEmail(UserPrincipal user, String changeEmailKey, EmailRequest request) throws Exception;
}


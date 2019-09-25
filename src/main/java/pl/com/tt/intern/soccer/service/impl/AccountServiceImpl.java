package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserInfoService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;
    private final ChangeAccountMailFactory accountMailFactory;
    private final ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;
    private final UserInfoService userInfoService;
    private final ChangeAccount changeAccountFactory;

    @Override
    public void activateAccountByConfirmationKey(String activationKey) throws IncorrectConfirmationKeyException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(activationKey);

            checkIfExpired(confirmationKey.getExpirationTime());
            confirmationKey.setExpirationTime(now());
            userService.changeEnabledAccount(confirmationKey.getUser(), true);
        } catch (NotFoundException e) {
            throw new IncorrectConfirmationKeyException("The account activation token can't be found in the database.");
        }
    }

    @Override
    public void setAndSendMailToChangePassword(String email) {
        String url = accountUrlGeneratorFactory.getUrlGenerator(AccountChangeType.valueOf(201)).generate(email, null);
        accountMailFactory.getMailSender(AccountChangeType.valueOf(201)).send(email, url);
    }

    @Override
    public void changePasswordNotLoggedInUser(String changePasswordKey, ForgottenPasswordRequest request)
            throws PasswordsMismatchException, IncorrectConfirmationKeyException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey);
            checkIfExpired(confirmationKey.getExpirationTime());

            if (request.getPassword().equals(request.getPasswordConfirmation())) {
                userService.changePassword(confirmationKey.getUser(), request.getPassword());
                confirmationKey.setExpirationTime(now());
            } else
                throw new PasswordsMismatchException();
        } catch (NotFoundException e) {
            throw new IncorrectConfirmationKeyException("The token can't be found in the database.");
        }
    }

    @Override
    public void deactivate(Long userId) throws NotFoundException {
        User user = userService.findById(userId);
        userService.changeEnabledAccount(user, false);
    }

    @Override
    public void changePasswordLoggedInUser(UserPrincipal userPrincipal, ChangePasswordRequest request)
            throws InvalidChangePasswordException {
        if (isPossibleChangePassword(request, userPrincipal.getPassword())) {
            userService.changePassword(
                    mapper.map(userPrincipal, User.class),
                    request.getNewPassword());
        } else throw new InvalidChangePasswordException("Incorrect old password or new passwords do not match.");
    }

    @Override
    public ChangeDataAccountResponse changeUserInfo(UserPrincipal userPrincipal, ChangeAccountDataRequest request) {
        UserInfo userInfo = mapper.map(userPrincipal, User.class).getUserInfo();
        userInfo.setFirstName(request.getFirstName());
        userInfo.setLastName(request.getLastName());
        userInfo.setPhone(request.getPhone());
        userInfo.setSkype(request.getSkype());
        return new ChangeDataAccountResponse(userInfoService.update(userInfo).getUser());
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws IncorrectConfirmationKeyException {
        if (!expirationTimeToken.isAfter(now()))
            throw new IncorrectConfirmationKeyException("The token has expired.");
    }

    private boolean isPossibleChangePassword(ChangePasswordRequest request, String oldPassword) {
        boolean matchesNewPassword = request.getNewPassword().equals(request.getNewPasswordConfirmation());
        boolean matchesOldPassword = encoder.matches(request.getOldPassword(), oldPassword);
        return matchesOldPassword && matchesNewPassword;
    }
}

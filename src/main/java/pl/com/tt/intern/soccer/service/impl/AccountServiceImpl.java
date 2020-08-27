package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.payload.request.EmailRequest;
import pl.com.tt.intern.soccer.payload.request.NewPasswordRequest;
import pl.com.tt.intern.soccer.payload.response.AccountInfoDataResponse;
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse;
import pl.com.tt.intern.soccer.payload.response.EmailChangeKeyResponse;
import pl.com.tt.intern.soccer.payload.response.PasswordChangeKeyResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.AccountService;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserInfoService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

import static java.time.LocalDateTime.now;
import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL;
import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD;
import static pl.com.tt.intern.soccer.account.url.enums.UrlParam.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    @Value("${mail.config.enabled}")
    private boolean enabledMail;
    private final ConfirmationKeyService confirmationKeyService;
    private final UserService userService;
    private final ChangeAccountMailFactory accountMailFactory;
    private final ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory;
    private final ModelMapper mapper;
    private final PasswordEncoder encoder;
    private final UserInfoService userInfoService;

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
    public PasswordChangeKeyResponse setAndSendMailToChangePassword(String email) throws NotFoundException {
        Map<UrlParam, String> params = new EnumMap<>(UrlParam.class);
        String uuid = confirmationKeyService.createAndAssignToUserByEmail(email).getUuid();

        if (!enabledMail) return new PasswordChangeKeyResponse(uuid);

        params.put(CHANGE_PASSWORD_KEY, uuid);
        accountMailFactory.getMailSender(NOT_LOGGED_IN_USER_PASSWORD).send(
                email,
                accountUrlGeneratorFactory
                        .getUrlGenerator(NOT_LOGGED_IN_USER_PASSWORD)
                        .generate(params));

        return new PasswordChangeKeyResponse();
    }

    @Override
    public EmailChangeKeyResponse setAndSendMailToChangeEmail(String email, String newEmail) throws NotFoundException {
        Map<UrlParam, String> params = new EnumMap<>(UrlParam.class);
        String uuid = confirmationKeyService.createAndAssignToUserByEmail(email).getUuid();

        if (!enabledMail) return new EmailChangeKeyResponse(uuid, newEmail);

        params.put(CHANGE_EMAIL_KEY, uuid);
        params.put(NEW_EMAIL, newEmail);
        accountMailFactory.getMailSender(EMAIL).send(
                email,
                accountUrlGeneratorFactory
                        .getUrlGenerator(EMAIL)
                        .generate(params));

        return new EmailChangeKeyResponse();
    }

    @Override
    public void changePasswordNotLoggedInUser(String confirmationKeyUUID, NewPasswordRequest request)
            throws PasswordsMismatchException, IncorrectConfirmationKeyException {
        try {
            ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(confirmationKeyUUID);
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
        UserInfo userInfo = userPrincipal.getUserInfo();
        userInfo.setFirstName(request.getFirstName());
        userInfo.setLastName(request.getLastName());
        userInfo.setPhone(request.getPhone());
        userInfo.setSkype(request.getSkype());
        return new ChangeDataAccountResponse(userInfoService.update(userInfo));
    }

    @SneakyThrows
    @Override
    public void changeEmail(UserPrincipal user, String confirmationKeyUUID, EmailRequest request)  {
        ConfirmationKey confirmationKey = confirmationKeyService.findConfirmationKeyByUuid(confirmationKeyUUID);
        checkIfExpired(confirmationKey.getExpirationTime());

        if (confirmationKey.getUser().getId().equals(
                mapper.map(user, User.class).getId())
        ) {
            userService.changeEmail(
                    confirmationKey.getUser(),
                    request.getEmail()
            );
        }
    }

    @Override
    public AccountInfoDataResponse getBasicInfoByUserInfoId(Long id) throws NotFoundException {
        UserInfo userInfo = userInfoService.findById(id);
        return mapper.map(userInfo, AccountInfoDataResponse.class);
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

    public void setEnabledMail(boolean enabledMail) {
        this.enabledMail = enabledMail;
    }
}

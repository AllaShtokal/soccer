package pl.com.tt.intern.soccer.service.impl;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.account.url.enums.UrlParam;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.RoleService;
import pl.com.tt.intern.soccer.service.SignUpService;
import pl.com.tt.intern.soccer.service.UserService;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singleton;
import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.ACTIVE_ACCOUNT;
import static pl.com.tt.intern.soccer.account.url.enums.UrlParam.ACTIVE_KEY;
import static pl.com.tt.intern.soccer.model.enums.RoleType.ROLE_USER;

@Slf4j
@Service
public class SignUpServiceImpl implements SignUpService {

    @Value("${mail.config.enabled}")
    private Boolean mailEnabled;

    private static final String SUCCESSFUL_SIGN_UP_MSG = "User registered successfully";
    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final ConfirmationKeyService confirmationKeyService;
    private final ChangeAccountMailFactory accountMailFactory;
    private final ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory;

    public SignUpServiceImpl(UserService userService,
                             RoleService roleService,
                             ModelMapper mapper,
                             ConfirmationKeyService confirmationKeyService,
                             ChangeAccountMailFactory accountMailFactory,
                             ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory) {
        this.userService = userService;
        this.roleService = roleService;
        this.mapper = mapper;
        this.confirmationKeyService = confirmationKeyService;
        this.accountMailFactory = accountMailFactory;
        this.accountUrlGeneratorFactory = accountUrlGeneratorFactory;
    }


    @Override
    public SuccessfulSignUpResponse signUp(SignUpRequest request) throws NotFoundException, PasswordsMismatchException {
        if (doPasswordsMatch(request)) {
            UserInfo userInfo = mapper.map(request, UserInfo.class);
            User user = mapper.map(request, User.class);

            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            user.setEnabled(true);
            user.setRoles(singleton(roleService.findByType(ROLE_USER)));
            User result = userService.save(user);

            return createKeyAndSendEmailIfIsEnabled(result);
        } else
            throw new PasswordsMismatchException();
    }

    @Override
    public Boolean ifUsernameIsTaken(String username) {
        return userService.existsByUsername(username);
    }

    @Override
    public Boolean ifEmailIsTaken(String email) {
        return userService.existsByEmail(email);
    }

    private SuccessfulSignUpResponse createKeyAndSendEmailIfIsEnabled(User user) throws NotFoundException {
        if (mailEnabled) {
            setAndSendActivationMailMsg(user);

            return new SuccessfulSignUpResponse(
                    SUCCESSFUL_SIGN_UP_MSG,
                    null,
                    user);
        } else
            return new SuccessfulSignUpResponse(
                    SUCCESSFUL_SIGN_UP_MSG,
                    confirmationKeyService.createAndAssignToUserByEmail(user.getEmail()).getUuid(),
                    user
            );
    }

    @SneakyThrows
    private void setAndSendActivationMailMsg(User user) {
        Map<UrlParam, String> params = new HashMap<>();
        params.put(ACTIVE_KEY, confirmationKeyService.createAndAssignToUserByEmail(user.getEmail()).getUuid());

        String url = accountUrlGeneratorFactory.getUrlGenerator(ACTIVE_ACCOUNT).generate(params);
        accountMailFactory.getMailSender(ACTIVE_ACCOUNT).send(user.getEmail(), url);
    }

    private boolean doPasswordsMatch(SignUpRequest request) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}

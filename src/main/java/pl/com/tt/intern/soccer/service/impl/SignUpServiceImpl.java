package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory;
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.service.RoleService;
import pl.com.tt.intern.soccer.service.SignUpService;
import pl.com.tt.intern.soccer.service.UserService;

import static java.util.Collections.singleton;
import static pl.com.tt.intern.soccer.model.enums.RoleType.ROLE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper mapper;
    private final ChangeAccountMailFactory accountMailFactory;
    private final ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory;

    @Override
    public SuccessfulSignUpResponse signUp(SignUpRequest request) throws Exception {
        if (doPasswordsMatch(request)) {
            UserInfo userInfo = mapper.map(request, UserInfo.class);
            User user = mapper.map(request, User.class);

            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            user.setRoles(singleton(roleService.findByType(ROLE_USER)));

            User result = userService.save(user);
           // setAndSendActivationMailMsg(result);
            return new SuccessfulSignUpResponse("User registered successfully", result);
        } else
            throw new PasswordsMismatchException();
    }

    private void setAndSendActivationMailMsg(User user) {
        String url = accountUrlGeneratorFactory.getUrlGenerator(AccountChangeType.valueOf(202)).generate(user.getEmail(), null);
        accountMailFactory.getMailSender(AccountChangeType.valueOf(202)).send(user.getEmail(), url);

    }

    private boolean doPasswordsMatch(SignUpRequest request) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}

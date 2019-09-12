package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
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

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;
    private final RoleService roleService;
    private final ModelMapper mapper;

    @Override
    public SuccessfulSignUpResponse signUp(SignUpRequest request) throws Exception {
        if (doPasswordsMatch(request)) {
            UserInfo userInfo = mapper.map(request, UserInfo.class);
            User user = mapper.map(request, User.class);

            userInfo.setUser(user);
            user.setUserInfo(userInfo);
            user.setRoles(singleton(roleService.findByType(ROLE_USER)));

            return new SuccessfulSignUpResponse("User registered successfully", userService.save(user));
        } else
            throw new PasswordsMismatchException();
    }

    private boolean doPasswordsMatch(SignUpRequest request) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}

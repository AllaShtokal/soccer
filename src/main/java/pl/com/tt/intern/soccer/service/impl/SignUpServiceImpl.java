package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.service.SignUpService;
import pl.com.tt.intern.soccer.service.UserService;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

    private final UserService userService;

    @Override
    public SuccessfulSignUpResponse signUp(SignUpRequest request) throws Exception {
        if (doPasswordsMatch(request)) {
            UserInfo userInfo = UserInfo.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            User user = User.builder()
                    .userInfo(userInfo)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(request.getPassword())
                    .build();

            return new SuccessfulSignUpResponse("User registered successfully", userService.save(user));
        } else
            throw new PasswordsMismatchException();
    }

    private boolean doPasswordsMatch(SignUpRequest request) {
        return request.getPassword().equals(request.getConfirmPassword());
    }
}

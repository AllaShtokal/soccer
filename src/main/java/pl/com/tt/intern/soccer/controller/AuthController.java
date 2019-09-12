package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.service.UserService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SuccessfulSignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) throws Exception {
        if (request.getPassword().equals(request.getConfirmPassword())) {
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

            return ok(new SuccessfulSignUpResponse("User registered successfully", userService.save(user)));
        } else
            throw new Exception("Passwords do not match");

    }


}

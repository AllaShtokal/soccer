package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.payload.request.LoginRequest;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.JwtAuthenticationResponse;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;
import pl.com.tt.intern.soccer.security.JwtTokenProvider;
import pl.com.tt.intern.soccer.service.LogInService;
import pl.com.tt.intern.soccer.service.SignUpService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final LogInService logInService;
    private final SignUpService signUpService;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = logInService.checkAndSetAuthentication(loginRequest);
        String jwt = tokenProvider.generateToken(authentication);
        return ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<SuccessfulSignUpResponse> signUp(@Valid @RequestBody SignUpRequest request)
            throws PasswordsMismatchException, NotFoundException {
        return ok(signUpService.signUp(request));
    }

    @GetMapping("/signup/test")
    public ResponseEntity<String> signUpForTests() throws PasswordsMismatchException, NotFoundException {
        int num = 8;
        for (int i = 0; i < num; i++) {

            signUpService.signUp(new SignUpRequest("user" + i, "u@user" + i, "String1!", "String1!", "user" + i, "user" + i));
        }

        return ok("added " + num + " users!");
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Boolean> ifUsernameIsTaken(@PathVariable String username) throws NotFoundException {
        return ok(signUpService.ifUsernameIsTaken(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Boolean> ifEmailIsTaken(@PathVariable String email) throws NotFoundException {
        return ok(signUpService.ifEmailIsTaken(email));
    }
}

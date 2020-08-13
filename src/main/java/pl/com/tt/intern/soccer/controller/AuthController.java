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

    @GetMapping("/username/{username}")
    public ResponseEntity<Boolean> ifUsernameIsTaken(@RequestParam("username") String username) throws NotFoundException {
        return ok(signUpService.ifUsernameIsTaken(username));
    }

    @GetMapping("/username/{email}")
    public ResponseEntity<Boolean> ifEmailIsTaken(@RequestParam("email") String email) throws NotFoundException {
        return ok(signUpService.ifEmailIsTaken(email));
    }
}

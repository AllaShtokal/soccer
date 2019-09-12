package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.payload.request.LoginRequest;
import pl.com.tt.intern.soccer.payload.response.JwtAuthenticationResponse;
import pl.com.tt.intern.soccer.security.JwtTokenProvider;
import pl.com.tt.intern.soccer.service.AuthenticationService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationService.checkAndSetAuthentication(loginRequest);
        String jwt = tokenProvider.generateToken(authentication);
        return ok(new JwtAuthenticationResponse(jwt));
    }

}

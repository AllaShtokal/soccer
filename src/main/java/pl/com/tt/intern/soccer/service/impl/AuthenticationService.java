package pl.com.tt.intern.soccer.service.impl;

import org.springframework.security.core.Authentication;
import pl.com.tt.intern.soccer.payload.request.LoginRequest;

public interface AuthenticationService {
    Authentication checkAndSetAuthentication(LoginRequest loginRequest);
}

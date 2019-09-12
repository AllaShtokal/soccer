package pl.com.tt.intern.soccer.service;

import org.springframework.security.core.Authentication;
import pl.com.tt.intern.soccer.payload.request.LoginRequest;

public interface AuthenticationService {
    Authentication checkAndSetAuthentication(LoginRequest loginRequest);
}

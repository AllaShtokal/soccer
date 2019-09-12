package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.payload.request.LoginRequest;
import pl.com.tt.intern.soccer.security.LogIn;
import pl.com.tt.intern.soccer.service.LogInService;

@Service
@RequiredArgsConstructor
public class LogInServiceImpl implements LogInService {

    private final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;

    @Override
    public Authentication checkAndSetAuthentication(LoginRequest loginRequest) {
        LogIn login = mapper.map(loginRequest, LogIn.class);
        Authentication authentication = checkAuthentication(login);
        setAuthentication(authentication);
        return authentication;
    }

    private void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication checkAuthentication(LogIn login) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.getUsernameOrEmail(),
                        login.getPassword()
                )
        );
    }
}

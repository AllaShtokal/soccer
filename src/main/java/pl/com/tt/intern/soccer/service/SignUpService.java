package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.payload.request.SignUpRequest;
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse;

public interface SignUpService {

    SuccessfulSignUpResponse signUp(SignUpRequest request) throws PasswordsMismatchException, NotFoundException;

    Boolean ifUsernameIsTaken(String username);
}

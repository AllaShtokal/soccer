package pl.com.tt.intern.soccer.controller

import org.springframework.security.core.Authentication
import pl.com.tt.intern.soccer.payload.request.LoginRequest
import pl.com.tt.intern.soccer.payload.request.SignUpRequest
import pl.com.tt.intern.soccer.payload.response.JwtAuthenticationResponse
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse
import pl.com.tt.intern.soccer.security.JwtTokenProvider
import pl.com.tt.intern.soccer.service.LogInService
import pl.com.tt.intern.soccer.service.SignUpService
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK

class AuthControllerTests extends Specification {

    JwtTokenProvider provider
    LogInService loginService
    SignUpService signupService
    AuthController controller

    LoginRequest loginRequest
    SignUpRequest signupRequest
    Authentication authentication

    def setup() {
        provider = Mock()
        loginService = Mock()
        signupService = Mock()
        controller = new AuthController(provider, loginService, signupService)

        loginRequest = Mock()
        signupRequest = Mock()
        authentication = Mock()
    }

    def "authenticateUser method should return OK status"() {
        when:
        def result = controller.authenticateUser(loginRequest).getStatusCode()

        then:
        result == OK
    }

    def "authenticateUser method should invoke LogInService.checkAndSetAuthentication method"() {
        when:
        controller.authenticateUser(loginRequest)

        then:
        1 * loginService.checkAndSetAuthentication(loginRequest) >> authentication
    }

    def "authenticateUser method should invoke JwtTokenProvider.generateToken method"() {
        when:
        controller.authenticateUser(loginRequest)

        then:
        1 * loginService.checkAndSetAuthentication(loginRequest) >> authentication
        1 * provider.generateToken(authentication)
    }

    def "authenticateUser method should return JwtAuthenticationResponse"() {
        given:
        JwtAuthenticationResponse response = new JwtAuthenticationResponse(null)

        when:
        def result = controller.authenticateUser(loginRequest).getBody()

        then:
        result == response
    }

    def "signUp method should return OK status"() {
        when:
        def result = controller.signUp(signupRequest).getStatusCode()

        then:
        result == OK
    }

    def "signUp method should return SuccessfulSignUpResponse"() {
        given:
        SuccessfulSignUpResponse response = Mock()

        when:
        def result = controller.signUp(signupRequest).getBody()

        then:
        1 * signupService.signUp(signupRequest) >> response
        result == response
    }

    def "signUp method should invoke SignUpService.signUp method"() {
        when:
        controller.signUp(signupRequest)

        then:
        1 * signupService.signUp(signupRequest) >> Mock(SuccessfulSignUpResponse)
    }

}

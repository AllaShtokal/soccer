package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.model.UserInfo
import pl.com.tt.intern.soccer.payload.request.SignUpRequest
import pl.com.tt.intern.soccer.payload.response.SuccessfulSignUpResponse
import pl.com.tt.intern.soccer.service.impl.SignUpServiceImpl
import spock.lang.Specification

class SignUpServiceTests extends Specification {

    UserService userService
    RoleService roleService
    ModelMapper mapper
    ChangeAccountMailFactory changeAccountMailFactory
    ChangeAccountUrlGeneratorFactory changeAccountUrlGeneratorFactory
    SignUpServiceImpl service

    def setup() {
        userService = Mock()
        roleService = Mock()
        mapper = Mock()
        changeAccountMailFactory = Mock()
        changeAccountUrlGeneratorFactory = Mock()

        service = new SignUpServiceImpl(
                userService,
                roleService,
                mapper,
                changeAccountMailFactory,
                changeAccountUrlGeneratorFactory
        )
    }

    def "doPasswordMatch should return true when both passwords are the same"() {
        given:
        SignUpRequest request = Mock()

        when:
        def result = service.doPasswordsMatch(request)

        then:
        1 * request.getPassword() >> "Password123"
        1 * request.getConfirmPassword() >> "Password123"
        result
    }

    def "doPasswordMatch should return false when both passwords are different"() {
        given:
        SignUpRequest request = Mock()

        when:
        def result = service.doPasswordsMatch(request)

        then:
        1 * request.getPassword() >> "Password123"
        1 * request.getConfirmPassword() >> "Pass1234567"
        !result
    }

    def "signUp method should return successful response if everything is ok"() {
        given:
        SignUpRequest request = new SignUpRequest()
        request.setPassword("Password123")
        request.setConfirmPassword("Password123")

        UserInfo userInfo = new UserInfo()
        userInfo.setFirstName("Test")
        userInfo.setLastName("Test")

        User user = new User()
        user.setEmail("test@test.com")
        user.setUserInfo(userInfo)

        SuccessfulSignUpResponse response = new SuccessfulSignUpResponse(
                "User registered successfully", user
        )

        when:
        def result = service.signUp(request)

        then:
        1 * mapper.map(request, User.class) >> user
        1 * mapper.map(request, UserInfo.class) >> userInfo
        1 * userService.save(user) >> user
        result == response
    }

    def "signUp method should throw an exception when passwords are different"() {
        given:
        SignUpRequest request = new SignUpRequest()
        request.setPassword("Password123")
        request.setConfirmPassword("Password1234")

        when:
        service.signUp(request)

        then:
        thrown(PasswordsMismatchException)
    }

}

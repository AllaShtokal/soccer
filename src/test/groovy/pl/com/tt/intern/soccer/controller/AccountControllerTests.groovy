package pl.com.tt.intern.soccer.controller


import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.AccountService
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK

class AccountControllerTests extends Specification {

    AccountService accountService
    AccountController controller

    def setup() {
        accountService = Mock()
        controller = new AccountController(accountService)
    }

    def "activateAccount method should return OK status"() {
        when:
        def result = controller.activateAccount().getStatusCode()

        then:
        result == OK
    }

    def "activateAccount method should invoke AccountService.activateAccountByConfirmationKey"() {
        given:
        String key = new String()

        when:
        controller.activateAccount(key)

        then:
        1 * accountService.activateAccountByConfirmationKey(key)
    }

    def "sendMailToChangePassword method should return OK status"() {
        when:
        def result = controller.activateAccount().getStatusCode()

        then:
        result == OK
    }

    def "sendMailToChangePassword method should invoke AccountService.setAndSendMailToChangePassword"() {
        given:
        String email = new String()

        when:
        controller.sendMailToChangePassword(email)

        then:
        1 * accountService.setAndSendMailToChangePassword(email)
    }

    def "changePasswordNotLoggedInUser method should return OK status"() {
        when:
        def result = controller.changePasswordNotLoggedInUser(new String(), Mock(ForgottenPasswordRequest)).getStatusCode()

        then:
        result == OK
    }

    def "changePasswordNotLoggedInUser method should invoke AccountService.setAndSendMailToChangePassword"() {
        given:
        String key = new String()
        ForgottenPasswordRequest request = Mock()

        when:
        controller.changePasswordNotLoggedInUser(key, request)

        then:
        1 * accountService.changePasswordNotLoggedInUser(key, request)
    }

    def "deactivateAccount method should return OK status"() {
        when:
        def result = controller.deactivateAccount(Mock(UserPrincipal)).getStatusCode()

        then:
        result == OK
    }

    def "deactivateAccount method should invoke AccountService.deactivate"() {
        given:
        UserPrincipal principal = Mock()
        Long id = 1

        when:
        principal.getId() >> id
        controller.deactivateAccount(principal)

        then:
        1 * accountService.deactivate(id)
    }

    def "changePasswordLoggedInUser method should return OK status"() {
        given:
        UserPrincipal principal = Mock()
        ChangePasswordRequest request = Mock()

        when:
        def result = controller.changePasswordLoggedInUser(principal, request).getStatusCode()

        then:
        result == OK
    }

    def "changePasswordLoggedInUser method should invoke AccountService.deactivate"() {
        given:
        UserPrincipal principal = Mock()
        ChangePasswordRequest request = Mock()

        when:
        controller.changePasswordLoggedInUser(principal, request)

        then:
        1 * accountService.changePasswordLoggedInUser(principal, request)
    }

}

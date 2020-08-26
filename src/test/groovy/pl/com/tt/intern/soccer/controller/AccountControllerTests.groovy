package pl.com.tt.intern.soccer.controller

import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest
import pl.com.tt.intern.soccer.payload.request.EmailRequest
import pl.com.tt.intern.soccer.payload.request.NewPasswordRequest
import pl.com.tt.intern.soccer.payload.response.AccountInfoDataResponse
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse
import pl.com.tt.intern.soccer.payload.response.EmailChangeKeyResponse
import pl.com.tt.intern.soccer.payload.response.PasswordChangeKeyResponse
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.AccountService
import spock.lang.Specification

import static org.springframework.http.HttpStatus.OK

class AccountControllerTests extends Specification {

    private final String EMAIL = "random.email@gmail.com"
    private final String NEW_EMAIL = "random.newemail@gmail.com"
    AccountService accountService
    AccountController controller

    def setup() {
        accountService = Mock()
        controller = new AccountController(accountService)
    }

    def "sendMailToChangePasswordIfEnabledAndAssignConfirmationKey method should return OK status"() {
        when:
        def result = controller
                .sendMailToChangePasswordIfEnabledAndAssignConfirmationKey(EMAIL)
                .getStatusCode()
        then:
        1 * accountService.setAndSendMailToChangePassword(EMAIL)
        result == OK
    }

    def "sendMailToChangePasswordIfEnabledAndAssignConfirmationKey method should invoke AccountService.setAndSendMailToChangePassword"() {
        when:
        controller.sendMailToChangePasswordIfEnabledAndAssignConfirmationKey(EMAIL)
        then:
        1 * accountService.setAndSendMailToChangePassword(EMAIL)
    }

    def "sendMailToChangePasswordIfEnabledAndAssignConfirmationKey method should return correct body"() {
        when:
        def result = controller
                .sendMailToChangePasswordIfEnabledAndAssignConfirmationKey(EMAIL)
                .getBody()
        then:
        1 * accountService.setAndSendMailToChangePassword(EMAIL) >> Mock(PasswordChangeKeyResponse)
        result instanceof PasswordChangeKeyResponse
    }

    def "sendMailToChangeEmailIfEnabledAndAssignConfirmationKey method should return OK status"() {
        when:
        def result = controller
                .sendMailToChangeEmailIfEnabledAndAssignConfirmationKey(EMAIL, NEW_EMAIL)
                .getStatusCode()
        then:
        1 * accountService.setAndSendMailToChangeEmail(EMAIL, NEW_EMAIL)
        result == OK
    }

    def "sendMailToChangeEmailIfEnabledAndAssignConfirmationKey method should invoke AccountService.setAndSendMailToChangePassword"() {
        when:
        controller.sendMailToChangeEmailIfEnabledAndAssignConfirmationKey(EMAIL, NEW_EMAIL)
        then:
        1 * accountService.setAndSendMailToChangeEmail(EMAIL, NEW_EMAIL)
    }

    def "sendMailToChangeEmailIfEnabledAndAssignConfirmationKey method should return correct body"() {
        when:
        def result = controller
                .sendMailToChangeEmailIfEnabledAndAssignConfirmationKey(EMAIL, NEW_EMAIL)
                .getBody()
        then:
        1 * accountService.setAndSendMailToChangeEmail(EMAIL, NEW_EMAIL) >> Mock(EmailChangeKeyResponse)
        result instanceof EmailChangeKeyResponse
    }

    def "activateAccount method should return OK status"() {
        given:
        String key = new String()
        when:
        def result = controller.activateAccount(key).getStatusCode()
        then:
        1 * accountService.activateAccountByConfirmationKey(key)
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

    def "changePasswordNotLoggedInUser method should return OK status"() {
        given:
        String key = new String()
        NewPasswordRequest request = Mock()
        when:
        def result = controller
                .changePasswordNotLoggedInUser(key, request)
                .getStatusCode()
        then:
        1 * accountService.changePasswordNotLoggedInUser(key, request)
        result == OK
    }

    def "changePasswordNotLoggedInUser method should invoke AccountService.setAndSendMailToChangePassword"() {
        given:
        String key = new String()
        NewPasswordRequest request = Mock()
        when:
        controller.changePasswordNotLoggedInUser(key, request)
        then:
        1 * accountService.changePasswordNotLoggedInUser(key, request)
    }

    def "deactivateAccount method should return OK status"() {
        given:
        UserPrincipal userPrincipal = Mock()
        when:
        def result = controller.deactivateAccount(userPrincipal).getStatusCode()
        then:
        1 * accountService.deactivate(userPrincipal.getId())
        result == OK
    }

    def "deactivateAccount method should invoke AccountService.deactivate"() {
        given:
        UserPrincipal userPrincipal = Mock()
        when:
        controller.deactivateAccount(userPrincipal)
        then:
        1 * accountService.deactivate(userPrincipal.getId())
    }

    def "changePasswordLoggedInUser method should return OK status"() {
        given:
        UserPrincipal userPrincipal = Mock()
        ChangePasswordRequest request = Mock()
        when:
        def result = controller
                .changePasswordLoggedInUser(userPrincipal, request)
                .getStatusCode()
        then:
        1 * accountService.changePasswordLoggedInUser(userPrincipal, request)
        result == OK
    }

    def "changePasswordLoggedInUser method should invoke AccountService.changePasswordLoggedInUser"() {
        given:
        UserPrincipal principal = Mock()
        ChangePasswordRequest request = Mock()
        when:
        controller.changePasswordLoggedInUser(principal, request)
        then:
        1 * accountService.changePasswordLoggedInUser(principal, request)
    }

    def "changeEmail method should return OK status and invoke AccountService.changeEmail"() {
        given:
        UserPrincipal userPrincipal = Mock()
        String key = new String()
        EmailRequest request = Mock()
        when:
        def result = controller.changeEmail(userPrincipal, key, request).getStatusCode()
        then:
        1 * accountService.changeEmail(userPrincipal, key, request)
        result == OK
    }

    def "changeBasicAccountData method should return OK status and invoke AccountService.changeUserInfo"() {
        given:
        UserPrincipal userPrincipal = Mock()
        ChangeAccountDataRequest request = Mock()
        when:
        def result = controller.changeBasicAccountData(userPrincipal, request).getStatusCode()
        then:
        1 * accountService.changeUserInfo(userPrincipal, request)
        result == OK
    }

    def "changeBasicAccountData method should return correct body"() {
        given:
        UserPrincipal userPrincipal = Mock()
        ChangeAccountDataRequest request = Mock()
        when:
        def result = controller.changeBasicAccountData(userPrincipal, request).getBody()
        then:
        1 * accountService.changeUserInfo(userPrincipal, request) >> Mock(ChangeDataAccountResponse)
        result instanceof ChangeDataAccountResponse
    }

    def "getBasicInfo method should return user info"() {
        given:
        UserPrincipal userPrincipal = Mock()
        when:
        def result = controller.getBasicInfo(userPrincipal).getBody()
        then:
        1 * accountService.getBasicInfoByUserInfoId(userPrincipal.getId()) >> Mock(AccountInfoDataResponse)
        result instanceof AccountInfoDataResponse
    }
}

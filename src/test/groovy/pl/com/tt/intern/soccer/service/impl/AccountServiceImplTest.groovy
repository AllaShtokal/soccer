package pl.com.tt.intern.soccer.service.impl

import org.modelmapper.ModelMapper
import org.springframework.security.crypto.password.PasswordEncoder
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory
import pl.com.tt.intern.soccer.account.mail.PerAccountTypeMailSender
import pl.com.tt.intern.soccer.account.url.AccountChangeUrlGenerator
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException
import pl.com.tt.intern.soccer.model.ConfirmationKey
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.model.UserInfo
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest
import pl.com.tt.intern.soccer.payload.request.EmailRequest
import pl.com.tt.intern.soccer.payload.request.NewPasswordRequest
import pl.com.tt.intern.soccer.payload.response.AccountInfoDataResponse
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse
import pl.com.tt.intern.soccer.payload.response.EmailChangeKeyResponse
import pl.com.tt.intern.soccer.payload.response.PasswordChangeKeyResponse
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ConfirmationKeyService
import pl.com.tt.intern.soccer.service.UserInfoService
import pl.com.tt.intern.soccer.service.UserService
import spock.lang.Specification

import java.time.LocalDateTime

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.EMAIL
import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.NOT_LOGGED_IN_USER_PASSWORD

class AccountServiceImplTest extends Specification {

    ConfirmationKeyService confirmationKeyService = Mock()
    UserService userService = Mock()
    ChangeAccountMailFactory accountMailFactory = Mock()
    ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory = Mock()
    PasswordEncoder encoder = Mock()
    AccountServiceImpl service
    UserInfoService userInfoService = Mock()
    ModelMapper mapper = Mock()
    AccountChangeUrlGenerator accountChangeUrlGenerator = Mock()
    PerAccountTypeMailSender perAccountTypeMailSender = Mock()

    ConfirmationKey confirmationKey
    User user

    def setup() {
        service = new AccountServiceImpl(confirmationKeyService, userService, accountMailFactory,
                accountUrlGeneratorFactory, mapper, encoder, userInfoService)

        user = new User()
        user.setId()
        confirmationKey = new ConfirmationKey(user)
        confirmationKey.setExpirationTime(LocalDateTime.now().plusMinutes(30))
        confirmationKey.setUuid("123456789")

    }

    def "getBasicInfoByUserInfoId method should return user info"() {
        given:
        UserInfo userInfo = new UserInfo()
        userInfo.setId(1L)
        userInfo.setFirstName("Kaziemirz")
        userInfo.setLastName("Wielki")
        userInfo.setPhone("123456789")
        userInfo.setSkype()

        when:
        def result = service.getBasicInfoByUserInfoId(1L)

        then:
        1 * userInfoService.findById(1L) >> userInfo
        1 * mapper.map(userInfo, AccountInfoDataResponse.class) >> Mock(AccountInfoDataResponse)
        result instanceof AccountInfoDataResponse
    }

    def "'activateAccountByConfirmationKey' method should activate account by confirmation key"() {
        given:
        def activationKey = "fdasfhih32rbhjfq3293r2fger"

        when:
        service.activateAccountByConfirmationKey(activationKey)
        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(activationKey) >> confirmationKey
        1 * userService.changeEnabledAccount(confirmationKey.getUser(), true)
    }

    def "'activateAccountByConfirmationKey' should throw IncorrectConfirmationKeyException if activationKey is wrong"() {
        given:
        def activationKey = "fdasfhih32rbhjfq3293r2fger"

        when:
        service.activateAccountByConfirmationKey(activationKey)

        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(activationKey) >> { throw new NotFoundException() }
        0 * userService.changeEnabledAccount(confirmationKey.getUser(), true)
        thrown(IncorrectConfirmationKeyException)
    }

    def "'setAndSendMailToChangePassword' method should return PasswordChangeKeyResponse by enableMail = false"() {
        given:
        def email = "user@user.com"
        service.setEnabledMail(false)


        when:
        service.setAndSendMailToChangePassword(email)
        then:
        1 * confirmationKeyService.createAndAssignToUserByEmail(email) >> confirmationKey
        0 * accountChangeUrlGenerator.generate(_) >> { email }
        0 * accountMailFactory.getMailSender(NOT_LOGGED_IN_USER_PASSWORD) >> perAccountTypeMailSender
        0 * perAccountTypeMailSender.send(_, _)
    }

    def "'setAndSendMailToChangePassword' method should return PasswordChangeKeyResponse by enableMail = true"() {
        given:
        def email = "user@user.com"
        service.setEnabledMail(true)

        when:
        def result = service.setAndSendMailToChangePassword(email)
        then:
        1 * confirmationKeyService.createAndAssignToUserByEmail(email) >> confirmationKey
        1 * accountUrlGeneratorFactory.getUrlGenerator(NOT_LOGGED_IN_USER_PASSWORD) >> accountChangeUrlGenerator
        1 * accountChangeUrlGenerator.generate(_) >> { email }
        1 * accountMailFactory.getMailSender(NOT_LOGGED_IN_USER_PASSWORD) >> perAccountTypeMailSender
        1 * perAccountTypeMailSender.send(_, _)
        result instanceof PasswordChangeKeyResponse
    }

    def "'setAndSendMailToChangeEmail' method should return EmailChangeKeyResponse by enableMail = false"() {
        given:
        def email = "email@user.com"
        def newEmail = "newEmail@user.com"
        service.setEnabledMail(false)

        when:
        def result = service.setAndSendMailToChangeEmail(email, newEmail)
        then:
        1 * confirmationKeyService.createAndAssignToUserByEmail(email) >> confirmationKey
        0 * accountUrlGeneratorFactory.getUrlGenerator(EMAIL) >> accountChangeUrlGenerator
        0 * accountChangeUrlGenerator.generate(_) >> { email }
        0 * accountMailFactory.getMailSender(EMAIL) >> perAccountTypeMailSender
        0 * perAccountTypeMailSender.send(_, _)
        result instanceof EmailChangeKeyResponse
    }

    def "'setAndSendMailToChangeEmail' method should return EmailChangeKeyResponse by enableMail = true"() {
        given:
        def email = "email@user.com"
        def newEmail = "newEmail@user.com"
        service.setEnabledMail(true)

        when:
        def result = service.setAndSendMailToChangeEmail(email, newEmail)
        then:
        1 * confirmationKeyService.createAndAssignToUserByEmail(email) >> confirmationKey
        1 * accountUrlGeneratorFactory.getUrlGenerator(EMAIL) >> accountChangeUrlGenerator
        1 * accountChangeUrlGenerator.generate(_) >> { email }
        1 * accountMailFactory.getMailSender(EMAIL) >> perAccountTypeMailSender
        1 * perAccountTypeMailSender.send(_, _)
        result instanceof EmailChangeKeyResponse
    }

    def "'changePasswordNotLoggedInUser' method should change password by confirmation key"() {
        given:
        def changePasswordKey = "asgag23r23FADFddSFasffDFG32"
        def request = new NewPasswordRequest()
        request.setPassword("1234567890")
        request.setPasswordConfirmation("1234567890")
        confirmationKey.setExpirationTime(LocalDateTime.now().plusMinutes(20))

        when:
        service.changePasswordNotLoggedInUser(changePasswordKey, request)
        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey) >> confirmationKey
        1 * userService.changePassword(_, _)
    }

    def "'changePasswordNotLoggedInUser' method throw PasswordsMismatchException if passwords is not correct"() {
        given:
        def changePasswordKey = "asgag23r23FADFddSFasffDFG32"
        def request = new NewPasswordRequest()
        request.setPassword("1234567890")
        request.setPasswordConfirmation("dasdasdsad")
        confirmationKey.setExpirationTime(LocalDateTime.now().plusMinutes(20))

        when:
        service.changePasswordNotLoggedInUser(changePasswordKey, request)
        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey) >> confirmationKey
        0 * userService.changePassword(_, _)
        thrown(PasswordsMismatchException)
    }

    def "'changePasswordNotLoggedInUser' method throw IncorrectConfirmationKeyException if not found confirmation key"() {
        given:
        def changePasswordKey = "asgag23r23FADFddSFasffDFG32"
        def request = new NewPasswordRequest()
        request.setPassword("1234567890")
        request.setPasswordConfirmation("dasdasdsad")

        when:
        service.changePasswordNotLoggedInUser(changePasswordKey, request)

        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(changePasswordKey) >> { throw new NotFoundException() }
        0 * userService.changePassword(_, _)
        thrown(IncorrectConfirmationKeyException)
    }

    def "'deactivate' method should deactivate account"() {
        given:
        def userId = 10

        when:
        service.deactivate(userId)

        then:
        1 * userService.findById(userId) >> user
        1 * userService.changeEnabledAccount(_, _)
    }

    def "'deactivate' method throw NotFoundException"() {
        given:
        def userId = 10

        when:
        service.deactivate(userId)

        then:
        1 * userService.findById(userId) >> { throw new NotFoundException() }
        0 * userService.changeEnabledAccount(_, _)
        thrown(NotFoundException)
    }

    def "'changePasswordLoggedInUser' method change password if user is logged by correct password"() {
        given:
        def userPrincipal = new UserPrincipal(1, null, "Tomek", false, true, null, "user@gmail.com", "1234")
        def request = new ChangePasswordRequest()
        request.setOldPassword("1234")
        request.setNewPassword("4321")
        request.setNewPasswordConfirmation("4321")

        when:
        service.changePasswordLoggedInUser(userPrincipal, request)

        then:
        1 * encoder.matches(request.getOldPassword(), _) >> { true }
        1 * mapper.map(userPrincipal, User.class) >> user
        1 * userService.changePassword(user, request.getNewPassword())
    }

    def "'changePasswordLoggedInUser' method throw InvalidChangePasswordException if user is logged by incorrect password"() {
        given:
        def userPrincipal = new UserPrincipal(1, null, "Tomek", false, true, null, "user@gmail.com", "41324134")
        def request = new ChangePasswordRequest()
        request.setOldPassword("1234")
        request.setNewPassword("4321")
        request.setNewPasswordConfirmation("4321")

        when:
        service.changePasswordLoggedInUser(userPrincipal, request)

        then:
        1 * encoder.matches(request.getOldPassword(), _) >> { false }
        0 * mapper.map(userPrincipal, User.class) >> user
        0 * userService.changePassword(user, request.getNewPassword())
        thrown(InvalidChangePasswordException)
    }

    def "'changeUserInfo' method should return ChangeDataAccountResponse"() {
        given:
        def userPrincipal = new UserPrincipal(1, null, "Tomek", false, true, null, "user@gmail.com", "41324134")
        def userInfo = new UserInfo()
        def userLocal = new User()
        userLocal.setEmail("user@gmail.com")
        userInfo.setUser(user)
        userPrincipal.setUserInfo(new UserInfo())

        def request = new ChangeAccountDataRequest()
        request.setFirstName("Tomek")
        request.setLastName("Kowalski")
        request.setPhone("123123123")
        request.setSkype("live:tomek")

        when:
        def result = service.changeUserInfo(userPrincipal, request)

        then:
        1 * userInfoService.update(_) >> { userInfo }
        result instanceof ChangeDataAccountResponse
    }

    def "'changeEmail' method should change email"() {
        given:
        def user2 = new UserPrincipal(1, null, "Tomek", false, true, null, "user@gmail.com", "41324134")
        def confirmationKeyUUID = "fads2334fsdfsf32fasfc"
        def request = new EmailRequest()
        request.setEmail("admin@gmail.com")
        def userAfterMapping = new User()
        userAfterMapping.setId(1)
        confirmationKey.setUser(userAfterMapping)

        when:
        service.changeEmail(user2, confirmationKeyUUID, request)

        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(confirmationKeyUUID) >> confirmationKey
        1 * mapper.map(user2, User.class) >> { userAfterMapping }
        1 * userService.changeEmail(confirmationKey.getUser(), request.getEmail())
    }

    def "'changeEmail' method should throw IncorrectConfirmationKeyException"() {
        given:
        def user2 = new UserPrincipal(1, null, "Tomek", false, true, null, "user@gmail.com", "41324134")
        def confirmationKeyUUID = "fads2334fsdfsf32fasfc"
        def request = new EmailRequest()
        request.setEmail("admin@gmail.com")
        def userAfterMapping = new User()
        userAfterMapping.setId(1)
        confirmationKey.setUser(userAfterMapping)

        def tokenAfterExpirationTime = new ConfirmationKey(new User())
        tokenAfterExpirationTime.setExpirationTime(LocalDateTime.now().minusMinutes(20))

        when:
        service.changeEmail(user2, confirmationKeyUUID, request)

        then:
        1 * confirmationKeyService.findConfirmationKeyByUuid(confirmationKeyUUID) >> { tokenAfterExpirationTime }
        thrown(IncorrectConfirmationKeyException)
        0 * mapper.map(user2, User.class) >> { userAfterMapping }
        0 * userService.changeEmail(confirmationKey.getUser(), request.getEmail())
    }
}


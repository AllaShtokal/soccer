package pl.com.tt.intern.soccer.service.impl

import org.modelmapper.ModelMapper
import org.springframework.security.crypto.password.PasswordEncoder
import pl.com.tt.intern.soccer.account.factory.ChangeAccountMailFactory
import pl.com.tt.intern.soccer.account.factory.ChangeAccountUrlGeneratorFactory
import pl.com.tt.intern.soccer.model.UserInfo
import pl.com.tt.intern.soccer.payload.response.AccountInfoDataResponse
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ConfirmationKeyService
import pl.com.tt.intern.soccer.service.UserInfoService
import pl.com.tt.intern.soccer.service.UserService
import spock.lang.Specification

class AccountServiceImplTest extends Specification {

    AccountServiceImpl accountService
    UserInfoService userInfoService
    ModelMapper mapper

    def setup() {
        ConfirmationKeyService confirmationKeyService = Mock()
        UserService userService = Mock()
        ChangeAccountMailFactory accountMailFactory = Mock()
        ChangeAccountUrlGeneratorFactory accountUrlGeneratorFactory = Mock()
        mapper = Mock()
        PasswordEncoder encoder = Mock()
        userInfoService = Mock()
        accountService = new AccountServiceImpl(confirmationKeyService, userService, accountMailFactory,
                accountUrlGeneratorFactory, mapper, encoder, userInfoService)
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
        def result = accountService.getBasicInfoByUserInfoId(1L)

        then:
        1 * userInfoService.findById(1L) >> userInfo
        1 * mapper.map(userInfo,AccountInfoDataResponse.class) >> Mock(AccountInfoDataResponse)
        result instanceof AccountInfoDataResponse

    }
}

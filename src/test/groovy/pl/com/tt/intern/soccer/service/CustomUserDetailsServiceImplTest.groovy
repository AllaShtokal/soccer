package pl.com.tt.intern.soccer.service

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.com.tt.intern.soccer.model.Role
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.model.enums.RoleType
import pl.com.tt.intern.soccer.repository.UserRepository
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.impl.CustomUserDetailsServiceImpl
import spock.lang.Specification

class CustomUserDetailsServiceImplTest extends Specification {

    CustomUserDetailsServiceImpl service
    UserPrincipal userDetails = Mock()
    User user = Mock()

    static Set<Role> roleSet

    def USERNAME_OR_EMAIL = "kek"

    def setup() {
        service = new CustomUserDetailsServiceImpl(userRepository: Mock(UserRepository))
        Role roleUser = new Role()
        roleUser.setType(RoleType.ROLE_USER)
        roleUser.setId(1L)
        Role roleAdmin = new Role()
        roleAdmin.setType(RoleType.ROLE_ADMIN)
        roleAdmin.setId(2L)
        roleSet = new HashSet<Role>()
        roleSet.add(roleUser)
        roleSet.add(roleAdmin)
    }

    def "verify if user is retrieved by checking its roles"() {
        given:
            user.getRoles() >> roleSet
            service.userRepository.findByUsernameOrEmail(USERNAME_OR_EMAIL, USERNAME_OR_EMAIL) >> Optional.of(user)
        when:
            UserPrincipal userDetailsFound = service.loadUserByUsername(USERNAME_OR_EMAIL)
        then:
            userDetailsFound.getAuthorities().size() == roleSet.size()
    }

    def "service should throw UsernameNotFoundException if findByUsernameOrEmail returns empty Optional"() {
        given:
            service.userRepository.findByUsernameOrEmail(USERNAME_OR_EMAIL, USERNAME_OR_EMAIL) >> Optional.empty()
        when:
            service.loadUserByUsername(USERNAME_OR_EMAIL)
        then:
            thrown(UsernameNotFoundException)
    }

}

package pl.com.tt.intern.soccer.service

import org.springframework.security.core.userdetails.UsernameNotFoundException
import pl.com.tt.intern.soccer.model.Role
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.model.enums.RoleType
import pl.com.tt.intern.soccer.repository.UserRepository
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.impl.CustomUserDetailsServiceImpl
import spock.lang.Specification

class CustomUserDetailsServiceTest extends Specification {

    CustomUserDetailsServiceImpl service
    User user = Mock()

    static Set<Role> roleSet
    def USERNAME_OR_EMAIL = "kek"
    def ID = 1

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

    def "verify if loadUserByUsername retrieves UserDetails with proper roles"() {
        when:
            UserPrincipal userDetailsFound = service.loadUserByUsername(USERNAME_OR_EMAIL)
        then:
            1 * service.userRepository.findByUsernameOrEmail(USERNAME_OR_EMAIL, USERNAME_OR_EMAIL) >> Optional.of(user)
            1 * user.getRoles() >> roleSet
            userDetailsFound.getAuthorities().size() == roleSet.size()
    }

    def "service should throw UsernameNotFoundException if findByUsernameOrEmail returns empty Optional"() {
        when:
            service.loadUserByUsername(USERNAME_OR_EMAIL)
        then:
            service.userRepository.findByUsernameOrEmail(USERNAME_OR_EMAIL, USERNAME_OR_EMAIL) >> Optional.empty()
            thrown(UsernameNotFoundException)
    }

    def "verify if loadUserById retrieves UserDetails with proper roles"() {
        when:
            UserPrincipal userDetailsFound = service.loadUserById(ID)
        then:
            user.getRoles() >> roleSet
            service.userRepository.findById(ID) >> Optional.of(user)
            userDetailsFound.getAuthorities().size() == roleSet.size()
    }

    def "findById should throw UsernameNotFoundException if empty Optional was returned"() {
        when:
            service.loadUserById(ID)
        then:
            service.userRepository.findById(ID) >> Optional.empty()
            thrown(UsernameNotFoundException)
    }
}

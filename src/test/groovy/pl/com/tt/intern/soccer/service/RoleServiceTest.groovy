package pl.com.tt.intern.soccer.service

import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.Role
import pl.com.tt.intern.soccer.model.enums.RoleType
import pl.com.tt.intern.soccer.repository.RoleRepository
import pl.com.tt.intern.soccer.service.impl.RoleServiceImpl
import spock.lang.Specification

class RoleServiceTest extends Specification {

    RoleRepository repository = Mock()
    RoleType roleType = RoleType.ROLE_USER
    RoleService service
    Role role = Mock()

    def setup() {
        service = new RoleServiceImpl(repository)
    }

    def "kek"() {
        given:
            repository.findByType(roleType) >> Optional.of(role)
        when:
            Role roleFound = service.findByType(roleType)
        then:
            roleFound == role
    }

    def "kek2"() {
        given:
            repository.findByType(roleType) >> Optional.empty()
        when:
            service.findByType(roleType)
        then:
            thrown(NotFoundException)
    }

}

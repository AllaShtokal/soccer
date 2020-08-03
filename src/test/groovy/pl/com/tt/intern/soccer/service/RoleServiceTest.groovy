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

    def "findByType should invoke repository->findByType once"() {
        when:
            Role roleFound = service.findByType(roleType)
        then:
            1 * repository.findByType(roleType) >> Optional.of(role)
            roleFound == role
    }

    def "findByType should throw NotFoundException if repository did no return any Role"() {
        when:
            service.findByType(roleType)
        then:
            1 * repository.findByType(roleType) >> Optional.empty()
            thrown(NotFoundException)
    }

}

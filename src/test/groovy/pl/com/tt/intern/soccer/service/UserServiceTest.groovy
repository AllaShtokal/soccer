package pl.com.tt.intern.soccer.service

import org.springframework.security.crypto.password.PasswordEncoder
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.repository.UserRepository
import pl.com.tt.intern.soccer.service.impl.UserServiceImpl
import spock.lang.Specification;

class UserServiceTest extends Specification {

    UserRepository repository = Mock()
    PasswordEncoder encoder = Mock()
    UserService service = new UserServiceImpl(repository, encoder)

    def ID = 1

    def setup() {

    }

    def "findAll should invoke repository->findAll()"() {
        when:
            service.findAll()
        then:
            1 * repository.findAll()
    }

    def "findById should invoke repository->findById()"() {
        given:
            User user = Mock()
            repository.findById(ID) >> Optional.of(user)
        when:
            User userFound = service.findById(ID)
        then:
            userFound == user
    }

    def "kek2"() {
        given:
            repository.findById(ID) >> Optional.empty()
        when:
            service.findById(ID)
        then:
            thrown(NotFoundException)
    }

}

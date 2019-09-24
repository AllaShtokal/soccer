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
    User user = Mock()
    def ID = 1
    def EMAIL = "email@test.com"
    def USERNAME = "username"
    def PASSWORD = "S4mP!3 Passw0rD"

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
            repository.findById(ID) >> Optional.of(user)
        when:
            User userFound = service.findById(ID)
        then:
            userFound == user
    }

    def "findById should throw exception if no user was found"() {
        given:
            repository.findById(ID) >> Optional.empty()
        when:
            service.findById(ID)
        then:
            thrown(NotFoundException)
    }

    def "savePassword should invoke encoder->encode() and repository->save()"() {
        given:
            user.getPassword() >> PASSWORD
        when:
            service.save(user)
        then:
            1 * encoder.encode(PASSWORD)
            1 * repository.save(_ as User)
    }

    def "deleteById"() {
        when:
            service.deleteById(ID)
        then:
            1 * repository.deleteById(ID)
    }

    def "findByEmail should invoke repository->findByEmail()"() {
        given:

            repository.findByEmail(EMAIL) >> Optional.of(user)
        when:
            User userFound = service.findByEmail(EMAIL)
        then:
            userFound == user
    }

    def "findByEmail should throw exception if no user was found"() {
        given:
            repository.findByEmail(EMAIL) >> Optional.empty()
        when:
            service.findByEmail(EMAIL)
        then:
            thrown(NotFoundException)
    }

    def "findByUsernameOrEmail should invoke repository->findByUsernameOrEmail()"() {
        given:

        repository.findByUsernameOrEmail(USERNAME, EMAIL) >> Optional.of(user)
        when:
        User userFound = service.findByUsernameOrEmail(USERNAME, EMAIL)
        then:
        userFound == user
    }

    def "findByUsernameOrEmail should throw exception if no user was found"() {
        given:
        repository.findByUsernameOrEmail(USERNAME, EMAIL) >> Optional.empty()
        when:
        service.findByUsernameOrEmail(USERNAME, EMAIL)
        then:
        thrown(NotFoundException)
    }

}

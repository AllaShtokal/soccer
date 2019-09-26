package pl.com.tt.intern.soccer.service

import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.ConfirmationKey
import pl.com.tt.intern.soccer.repository.ConfirmationKeyRepository
import pl.com.tt.intern.soccer.service.impl.ConfirmationKeyServiceImpl
import spock.lang.Specification

class ConfirmationKeyServiceTest extends Specification {

    ConfirmationKeyService service
    ConfirmationKeyRepository repository = Mock()
    UserService userService = Mock()
    ConfirmationKey key = Mock()
    def UUID = "12451251"

    def setup() {
        service = new ConfirmationKeyServiceImpl(repository, userService)
    }

    def "findById"() {
        when:
            service.save(key)
        then:
            1 * repository.save(key)
    }

    def "findByEmail should invoke repository->findByEmail()"() {
        when:
            ConfirmationKey keyFound = service.findConfirmationKeyByUuid(UUID)
        then:
            1 * repository.findByUuid(UUID) >> Optional.of(key)
            keyFound == key
    }

    def "findByEmail should throw exception if no user was found"() {
        when:
            service.findConfirmationKeyByUuid(UUID)
        then:
            1 * repository.findByUuid(UUID) >> Optional.empty()
            thrown(NotFoundException)
    }

    def "scanAndDeleteExpiredConfirmationKeys"() {
        when:
            service.scanAndDeleteExpiredConfirmationKeys()
        then:
            repository.deleteByExpirationTime()
    }
}

package pl.com.tt.intern.soccer.service

import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.ConfirmationKey
import pl.com.tt.intern.soccer.repository.ConfirmationKeyRepository
import pl.com.tt.intern.soccer.service.impl.ConfirmationKeyServiceImpl
import spock.lang.Specification

class ConfirmationKeyServiceTest extends Specification {

    ConfirmationKeyService service
    ConfirmationKeyRepository repository = Mock()
    ConfirmationKey key = Mock()
    def UUID = "12451251"

    def setup() {
        service = new ConfirmationKeyServiceImpl(repository)
    }

    def "findById"() {
        when:
            service.save(key)
        then:
            1 * repository.save(key)
    }

    def "findByEmail should invoke repository->findByEmail()"() {
        given:
            repository.findByUuid(UUID) >> Optional.of(key)
        when:
            ConfirmationKey keyFound = service.findConfirmationKeyByUuid(UUID)
        then:
            keyFound == key
    }

    def "findByEmail should throw exception if no user was found"() {
        given:
            repository.findByUuid(UUID) >> Optional.empty()
        when:
            service.findConfirmationKeyByUuid(UUID)
        then:
            thrown(NotFoundException)
    }

    def "scanAndDeleteExpiredConfirmationKeys"() {
        when:
            service.scanAndDeleteExpiredConfirmationKeys()
        then:
            repository.deleteByExpirationTime()
    }
}

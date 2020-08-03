package pl.com.tt.intern.soccer.service

import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.UserInfo
import pl.com.tt.intern.soccer.repository.UserInfoRepository
import pl.com.tt.intern.soccer.service.impl.UserInfoServiceImpl
import spock.lang.Specification

import static java.util.Arrays.asList

class UserInfoServiceTests extends Specification {

    UserInfoRepository repository
    UserInfoService service
    UserInfo userInfo
    Long id

    def setup() {
        repository = Mock()
        service = new UserInfoServiceImpl(repository)
        userInfo = Mock()
        id = 1
    }

    def "findAll method should return a list of UserInfo"() {
        given:
        def list = asList(userInfo)

        when:
        def result = service.findAll()

        then:
        1 * repository.findAll() >> list
        result == list
    }

    def "findById method should return a UserInfo if found"() {
        when:
        def result = service.findById(id)

        then:
        1 * repository.findById(id) >> Optional.of(userInfo)
        result == userInfo
    }

    def "findById method should throw an exception if UserInfo not found"() {
        when:
        service.findById(id)

        then:
        1 * repository.findById(id) >> Optional.empty()
        thrown(NotFoundException)
    }

    def "save method should return UserInfo"() {
        when:
        def result = service.save(userInfo)

        then:
        1 * repository.save(userInfo) >> userInfo
        result == userInfo
    }

    def "deleteById method should invoke UserInfoRepository.deleteById"() {
        when:
        service.deleteById(id)

        then:
        1 * repository.deleteById(id)
    }

}

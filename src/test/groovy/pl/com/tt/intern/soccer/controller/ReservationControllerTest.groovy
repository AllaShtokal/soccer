package pl.com.tt.intern.soccer.controller

import org.springframework.http.HttpStatus
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.payload.response.ReservationResponse
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

import static java.time.DayOfWeek.MONDAY
import static java.time.DayOfWeek.SUNDAY
import static java.time.LocalDateTime.now
import static org.springframework.http.HttpStatus.OK
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

class ReservationControllerTest extends Specification {

    ReservationController controller
    ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
    ReservationService service = Mock()
    def ID = 1
    def userId = 2
    
    def setup() {
        controller = new ReservationController(service)
    }

    def "deleteReservation should invoke ReservationService.deleteById"() {
        given:
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            service.existsByIdAndByUserId(ID, userId) >> true
        when:
            controller.deleteOwnReservation(user, ID)
        then:
            with(service) {
                1 * deleteById(ID)
            }
    }

    def "update method should be invoked when reservation exists"() {
        given:
            service.existsByIdAndByUserId(_,_) >> true
        when:
            controller.editOwnReservation(Mock(UserPrincipal), ID , reservationPersistRequest)
        then:
            with(service) {
                1 * update(ID, reservationPersistRequest)
            }
    }

    def "update method should throw exception when reservation does not exist"() {
        given:
            service.existsByIdAndByUserId(_ as Long, _ as Long) >> false
        when:
            controller.editOwnReservation(Mock(UserPrincipal), ID, reservationPersistRequest)
        then:
            thrown(NotFoundException)
    }

    def "saveNewReservationWithOwnId should invoke verification and save"() {
        given:
            ReservationPersistRequest reservationPersistRequest = Mock()
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            service.verifyPersistedObject() >> {}
        when:
            controller.saveNewReservationWithOwnId(user, reservationPersistRequest)
        then:
            with(service) {
                1 * verifyPersistedObject(reservationPersistRequest)
                1 * save(reservationPersistRequest, userId)
            }
    }

    def "saveNewReservation should return CREATED status"() {
        given:
            ReservationPersistRequest reservationPersistRequest = Mock()
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            service.verifyPersistedObject() >> {}
        expect:
            controller.saveNewReservationWithOwnId(user, reservationPersistRequest).getStatusCode() == HttpStatus.CREATED
    }

    def "findByPeriod method should return OK status"() {
        given:
        def period = TODAY

        when:
        def status = controller.findByPeriod(period).getStatusCode()

        then:
        status == OK
    }

    def "findByDay method should return OK status"() {
        given:
        def day = MONDAY

        when:
        def status = controller.findByDay(day).getStatusCode()

        then:
        status == OK
    }

    def "findByDate method should return OK status"() {
        given:
        def date = new ReservationDateRequest(now().withHour(10), now().withHour(20))

        when:
        def status = controller.findByDate(date).getStatusCode()

        then:
        status == OK
    }

    def "findByPeriod method should return list of ReservationResponse"() {
        given:
        def period = TODAY
        def list = new ArrayList<ReservationResponse>()

        when:
        def body = controller.findByPeriod(period).getBody()

        then:
        1 * service.findByPeriod(period) >> list
        body == list
    }

    def "findByDay method should return list of ReservationResponse"() {
        given:
        def day = SUNDAY
        def list = new ArrayList<ReservationResponse>()

        when:
        def body = controller.findByDay(day).getBody()

        then:
        1 * service.findByDay(day) >> list
        body == list
    }

    def "findByDate method should return list of ReservationResponse"() {
        given:
        def date = new ReservationDateRequest(now().withHour(10), now().withHour(20))
        def list = new ArrayList<ReservationResponse>()

        when:
        def body = controller.findByDate(date).getBody()

        then:
        1 * service.findByDateBetween(date) >> list
        body == list
    }
}

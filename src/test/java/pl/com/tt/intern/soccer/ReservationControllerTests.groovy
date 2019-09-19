package pl.com.tt.intern.soccer


import org.springframework.security.test.context.support.WithMockUser
import pl.com.tt.intern.soccer.controller.ReservationController
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import pl.com.tt.intern.soccer.payload.response.ReservationResponse
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

import static java.time.DayOfWeek.MONDAY
import static java.time.DayOfWeek.SUNDAY
import static java.time.LocalDateTime.now
import static org.springframework.http.HttpStatus.OK
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

@WithMockUser
class ReservationControllerTests extends Specification {

    ReservationService service = Mock()
    ReservationController controller = new ReservationController(service)

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

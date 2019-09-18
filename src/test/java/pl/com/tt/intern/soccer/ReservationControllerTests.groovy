package pl.com.tt.intern.soccer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.com.tt.intern.soccer.controller.ReservationController
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import spock.lang.Specification

import static java.time.DayOfWeek.MONDAY
import static java.time.LocalDateTime.now
import static org.springframework.http.HttpStatus.OK
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

@SpringBootTest
class ReservationControllerTests extends Specification {

    @Autowired
    ReservationController controller

    def "should findByPeriod method return OK status"() {
        given:
        def period = TODAY

        when:
        def result = controller.findByPeriod(period).getStatusCode()

        then:
        result == OK
    }

    def "should findByDay method return OK status"() {
        given:
        def day = MONDAY

        when:
        def result = controller.findByDay(day).getStatusCode()

        then:
        result == OK
    }

    def "should findByDate method return OK status"() {
        given:
        def date = new ReservationDateRequest(now().withHour(10), now().withHour(20))

        when:
        def result = controller.findByDate(date).getStatusCode()

        then:
        result == OK
    }

}

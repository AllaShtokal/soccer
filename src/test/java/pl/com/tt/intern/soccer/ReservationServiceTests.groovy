package pl.com.tt.intern.soccer

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.model.Reservation
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import pl.com.tt.intern.soccer.payload.response.ReservationResponse
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.ReservationService
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import static java.time.DayOfWeek.MONDAY
import static java.time.LocalDateTime.now
import static java.util.Arrays.asList
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

class ReservationServiceTests extends Specification {

    private final static Long id = 1

    ReservationRepository repository
    ModelMapper mapper
    ReservationService service
    ReservationResponse response
    Reservation reservation

    def setup() {
        repository = Mock(ReservationRepository)
        mapper = Mock()
        service = new ReservationServiceImpl(repository, mapper)
        response = Mock()
        reservation = Mock()
    }

    def "'findAll' method should return list of ReservationResponse"() {
        given:
        def list = asList(reservation)

        when:
        def result = service.findAll()

        then:
        1 * repository.findAll() >> list
        1 * mapper.map(reservation, ReservationResponse) >> response
        result.size() == 1
        result.get(0) == response
    }

    def "'findById' method should return ReservationResponse if found"() {
        when:
        def result = service.findById(id)

        then:
        1 * repository.findById(id) >> Optional.of(reservation)
        1 * mapper.map(reservation, ReservationResponse) >> response
        result == response
    }

    def "'findById' method should throw a NotFoundException if not found"() {
        when:
        def result = service.findById(id)

        then:
        1 * repository.findById(id) >> Optional.empty()
        thrown(NotFoundException)
    }

    def "'save' method should return ReservationResponse after save"() {
        when:
        def result = service.save(reservation)

        then:
        1 * repository.save(reservation) >> reservation
        1 * mapper.map(reservation, ReservationResponse) >> response
        result == response
    }

    def "'findByDateBetween' method should return a list of ReservationResponse"() {
        given:
        ReservationDateRequest request = Mock()
        def list = asList(reservation)
        def responses = asList(response)

        when:
        def result = service.findByDateBetween(request)

        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(request.getFrom(), request.getTo()) >> list
        mapper.map(reservation, ReservationResponse) >> response
        result == responses
    }

    def "'findByPeriod' method should return a list of ReservationResponse"() {
        given:
        def period = TODAY
        def list = asList(reservation)
        def responses = asList(response)

        when:
        def result = service.findByPeriod(period)

        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(period.from(), period.to()) >> list
        mapper.map(reservation, ReservationResponse) >> response
        result == responses
    }

    def "'findByDay' method should return a list of ReservationResponse"() {
        given:
        def day = MONDAY
        def list = asList(reservation)
        def responses = asList(response)
        def from = now().with(day)
                .withHour(0).withMinute(0).withSecond(0).withNano(0)
        def to = now().with(day)
                .withHour(23).withMinute(59).withSecond(59).withNano(0)

        when:
        def result = service.findByDay(day)

        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(from, to) >> list
        mapper.map(reservation, ReservationResponse) >> response
        result == responses
    }

}

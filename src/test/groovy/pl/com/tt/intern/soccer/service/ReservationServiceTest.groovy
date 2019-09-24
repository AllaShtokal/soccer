package pl.com.tt.intern.soccer.service


import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.exception.ReservationClashException
import pl.com.tt.intern.soccer.exception.ReservationFormatException
import pl.com.tt.intern.soccer.model.Reservation
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.payload.response.ReservationResponse
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

import static java.time.DayOfWeek.MONDAY
import static java.time.LocalDateTime.now
import static java.util.Arrays.asList
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

class ReservationServiceTest extends Specification {

    ReservationRepository repository = Mock()
    UserService userService = Mock()
    ModelMapper mapper = Mock()
    ReservationPersistRequest reservationPersistRequest = Mock()
    ReservationService service
    ReservationResponse response
    Reservation reservation
    def ID = 1
    static LocalDateTime timeNow = LocalDateTime.now()

    def setup() {
        service = new ReservationServiceImpl(repository, userService, mapper)
        response = Mock()
        reservation = Mock()
    }

    def "deleteById should invoke ReservationRepository.deleteById"() {
        when:
        service.deleteById(ID)
        then:
        with(repository) {
            1 * deleteById(ID)
        }
    }


    def "isDateRangeAvailable should return true if there are no date collisions"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.now()
        LocalDateTime timeTo = LocalDateTime.now().plusDays(1)
        Reservation reservation = Mock(Reservation)
        reservation.getId() >> ID
        ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        repository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> true

        expect:
        service.datesCollideWithExistingReservationsExcludingEditedOne(reservationPersistRequest, reservation)
    }

    def "isDateRangeAvailable should return false if there are date collisions"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.now()
        LocalDateTime timeTo = LocalDateTime.now().plusDays(1)
        Reservation reservation = Mock(Reservation)
        reservation.getId() >> ID
        ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        repository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> false

        expect:
        !service.datesCollideWithExistingReservationsExcludingEditedOne(reservationPersistRequest, reservation)
    }

    def "isDateRangeAvailable should return false if there is any date collision"() {
        given:
        LocalDateTime time1 = LocalDateTime.now()
        LocalDateTime time2 = LocalDateTime.now().plusDays(1)
        repository.datesCollide(time1, time2) >> true
        expect:
        service.datesCollideWithExistingReservations(time1, time2)
    }

    def "isDate15MinuteRounded should consider minutes, seconds and nanoseconds"() {
        given:
        LocalDateTime time = LocalDateTime.of(2019, Month.JULY, 1, hour, min, sec, nano)
        expect:
        service.isDate15MinuteRounded(time) == expected
        where:
        hour | min | sec | nano | expected
        0    | 0   | 0   | 0    | true
        12   | 0   | 0   | 0    | true
        23   | 15  | 0   | 0    | true
        7    | 30  | 0   | 0    | true
        7    | 45  | 0   | 0    | true
        13   | 1   | 0   | 0    | false
        14   | 2   | 0   | 0    | false
        15   | 3   | 0   | 0    | false
        16   | 5   | 0   | 0    | false
        17   | 6   | 0   | 0    | false
        18   | 7   | 0   | 0    | false
        19   | 8   | 0   | 0    | false
        20   | 9   | 0   | 0    | false
        21   | 10  | 0   | 0    | false
        22   | 12  | 0   | 0    | false
        8    | 15  | 1   | 0    | false
        9    | 15  | 0   | 1    | false
    }

    def "isDateOrderOk should only return true if dateFrom is smaller than dateTo"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(2019, Month.JULY, day1, hour1, min1, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(2019, Month.JULY, day2, hour2, min2, 0, 0)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        expect:
        service.isDateOrderOk(reservationPersistRequest) == expected
        where:
        day1 | hour1 | min1 | day2 | hour2 | min2 | expected
        15   | 12    | 30   | 15   | 12    | 45   | true
        15   | 12    | 30   | 15   | 12    | 30   | false
        15   | 12    | 30   | 14   | 12    | 30   | false
    }

    def "isInFuture should return true if persisted dto has future date"() {
        given:
        reservationPersistRequest.getDateFrom() >> LocalDateTime.now().plusDays(1L)
        expect:
        service.isInFuture(reservationPersistRequest)
    }

    def "isInFuture should return false if persisted dto has past date"() {
        given:
        reservationPersistRequest.getDateFrom() >> LocalDateTime.now().minusDays(1L)
        expect:
        !service.isInFuture(reservationPersistRequest)
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
        def result = service.findById(ID)

        then:
        1 * repository.findById(ID) >> Optional.of(reservation)
        1 * mapper.map(reservation, ReservationResponse) >> response
        result == response
    }

    def "'findById' method should throw a NotFoundException if not found"() {
        when:
        def result = service.findById(ID)

        then:
        1 * repository.findById(ID) >> Optional.empty()
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

    def "update should throw NotFoundException if reservation was not found"() {
        given:
            repository.findById(ID) >> Optional.empty()
        when:
            service.update(ID, reservationPersistRequest)
        then:
            thrown(NotFoundException)
    }

    def "update should throw ReservationFormatException if contains invalid date format"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(year1, Month.MAY, day1, hour1, min1, 0, 0)
            LocalDateTime timeTo = LocalDateTime.of(year2, Month.MAY, day2, hour2, min2, 0, 0)
            repository.findById(ID) >> Optional.of(reservation)
            reservationPersistRequest.getDateFrom() >> timeFrom
            reservationPersistRequest.getDateTo() >> timeTo
        when:
            service.update(ID, reservationPersistRequest)
        then:
            thrown(ReservationFormatException)
        where:
            year1            | day1 | hour1 | min1 | year2              |  day2 | hour2 | min2
            timeNow.year - 1 | 15   | 12    | 30   | timeNow.year - 1   |  15   | 12    | 15 // no future
            timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1   |  15   | 12    | 44 //no round 15 min
            timeNow.year + 1 | 15   | 12    | 31   | timeNow.year + 1   |  15   | 12    | 45 //no round 15 min
            timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1   |  15   | 12    | 15 //wrong order
    }

    def "update should throw ReservationClashException if reservation date range is already booked"() {
        given:
            def reservationID = 540293812
            LocalDateTime timeFrom = LocalDateTime.of(timeNow.year+1, Month.MAY, 11, 2, 15, 0, 0)
            LocalDateTime timeTo = LocalDateTime.of(timeNow.year+1, Month.MAY, 11, 2, 30, 0, 0)
            repository.findById(ID) >> Optional.of(reservation)
            reservation.getId() >> reservationID
            reservationPersistRequest.getDateFrom() >> timeFrom
            reservationPersistRequest.getDateTo() >> timeTo
            repository.datesCollideExcludingCurrent(timeFrom, timeTo, reservationID) >> true
        when:
            service.update(ID, reservationPersistRequest)
        then:
            thrown(ReservationClashException)
    }

    def "update should invoke repository.save method if reservation validation passes"() {
        given:
            def reservationID = 540293812
            LocalDateTime timeFrom = LocalDateTime.of(timeNow.year+1, Month.MAY, 11, 2, 15, 0, 0)
            LocalDateTime timeTo = LocalDateTime.of(timeNow.year+1, Month.MAY, 11, 2, 30, 0, 0)
            repository.findById(ID) >> Optional.of(reservation)
            reservation.getId() >> reservationID
            reservationPersistRequest.getDateFrom() >> timeFrom
            reservationPersistRequest.getDateTo() >> timeTo
            repository.datesCollideExcludingCurrent(timeFrom, timeTo, reservationID) >> false
        when:
            service.update(ID, reservationPersistRequest)
        then:
            1 * repository.save(_ as Reservation)
    }

    def "existsById should invoke repository->existsById"() {
        when:
            service.existsById(ID)
        then:
            with(repository){
                1 * existsById(ID)
            }
    }

    def "existsByIdAndByUserId should invoke repository->existsByIdAndByUserId"() {
        given:
            def userID = 95133151
        when:
            service.existsByIdAndByUserId(ID, userID)
        then:
            with(repository){
                1 * existsByIdAndUserId(ID, userID)
            }
    }
}

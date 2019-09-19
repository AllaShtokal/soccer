package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.model.Reservation
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class ReservationServiceTest extends Specification {

    ReservationRepository reservationRepository = Mock()
    UserService userService = Mock()
    ModelMapper mapper = Mock()
    ReservationPersistRequest reservationPersistDTO = Mock()
    ReservationService reservationService
    ReservationRepository reservationRepository = Mock(ReservationRepository)

    def ID = 1

    def setup() {
        reservationService = new ReservationServiceImpl(reservationRepository, userService, mapper)
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
            reservationRepository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> false

        expect:
            reservationService.isDateRangeAvailableForEdit(reservationPersistRequest, reservation) == true
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
            reservationRepository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> true

        expect:
            reservationService.isDateRangeAvailableForEdit(reservationPersistRequest, reservation) == false
    }


            LocalDateTime time1 = LocalDateTime.now()
            LocalDateTime time2 = LocalDateTime.now().plusDays(1)
            reservationRepository.datesCollide(time1, time2) >> false
        expect:
            reservationService.isDateRangeAvailable(time1,time2) == true
    }

    def "isDateRangeAvailable should return false if there is any date collision"() {
        given:
            LocalDateTime time1 = LocalDateTime.now()
            LocalDateTime time2 = LocalDateTime.now().plusDays(1)
            reservationRepository.datesCollide(time1, time2) >> true
        expect:
            reservationService.isDateRangeAvailable(time1,time2) == false
    }

    def "isDate15MinuteRounded should consider minutes, seconds and nanoseconds"() {
        given:
            LocalDateTime time = LocalDateTime.of(2019, Month.JULY, 1, hour, min, sec, nano)
        expect:
            reservationService.isDate15MinuteRounded(time) == expected
        where:
            hour    | min     | sec       | nano      | expected
            0       | 0       | 0         | 0         | true
            12      | 0       | 0         | 0         | true
            23      | 15      | 0         | 0         | true
            7       | 30      | 0         | 0         | true
            7       | 45      | 0         | 0         | true
            13      | 1       | 0         | 0         | false
            14      | 2       | 0         | 0         | false
            15      | 3       | 0         | 0         | false
            16      | 5       | 0         | 0         | false
            17      | 6       | 0         | 0         | false
            18      | 7       | 0         | 0         | false
            19      | 8       | 0         | 0         | false
            20      | 9       | 0         | 0         | false
            21      | 10      | 0         | 0         | false
            22      | 12      | 0         | 0         | false
            8       | 15      | 1         | 0         | false
            9       | 15      | 0         | 1         | false
    }

    def "isDateOrderOk should only return true if dateFrom is smaller than dateTo"() {
        given:
            LocalDateTime timeFrom = LocalDateTime.of(2019, Month.JULY, day1, hour1, min1, 0, 0)
            LocalDateTime timeTo = LocalDateTime.of(2019, Month.JULY, day2, hour2, min2, 0, 0)
            reservationPersistDTO.getDateFrom() >> timeFrom
            reservationPersistDTO.getDateTo() >> timeTo
        expect:
            reservationService.isDateOrderOk(reservationPersistDTO) == expected
        where:
            day1| hour1 | min1   | day2  | hour2 | min2  | expected
            15  | 12    | 30     | 15    | 12    | 45    | true
            15  | 12    | 30     | 15    | 12    | 30    | false
            15  | 12    | 30     | 14    | 12    | 30    | false
    }

    def "isInFuture should return true if persisted dto has future date" () {
        given:
            reservationPersistDTO.getDateFrom() >> LocalDateTime.now().plusDays(1L);
        expect:
            reservationService.isInFuture(reservationPersistDTO) == true
    }

    def "isInFuture should return false if persisted dto has past date" () {
        given:
            reservationPersistDTO.getDateFrom() >> LocalDateTime.now().minusDays(1L);
        expect:
            reservationService.isInFuture(reservationPersistDTO) == false
    }
}

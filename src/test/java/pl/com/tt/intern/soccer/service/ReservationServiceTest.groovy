package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.dto.ReservationPersistDTO
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

class ReservationServiceTest extends Specification {

    ReservationService reservationService
    ReservationRepository reservationRepository = Mock(ReservationRepository)
    UserService userService = Mock(UserService)
    ModelMapper mapper = Mock(ModelMapper)
    ReservationPersistDTO reservationPersistDTO = Mock(ReservationPersistDTO)

    def setup() {
        reservationService = new ReservationServiceImpl(reservationRepository, userService, mapper)
    }

    def "isDateRangeAvailable should return true if there are no date collisions"() {
        given: reservationRepository.datesCollide(_ as LocalDateTime, _ as LocalDateTime) >> false
        expect: reservationService.isDateRangeAvailable(reservationPersistDTO) == true
    }

    def "isDateRangeAvailable should return false if there is any date collision"() {
        given: reservationRepository.datesCollide(_, _) >> true
        expect: reservationService.isDateRangeAvailable(reservationPersistDTO) == false
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
        given: reservationPersistDTO.getDateFrom() >> LocalDateTime.now().plusDays(1L);
        expect: reservationService.isInFuture(reservationPersistDTO) == true
    }

    def "isInFuture should return false if persisted dto has past date" () {
        given: reservationPersistDTO.getDateFrom() >> LocalDateTime.now().minusDays(1L);
        expect: reservationService.isInFuture(reservationPersistDTO) == false
    }
}

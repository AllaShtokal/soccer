package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.model.Reservation
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import java.time.LocalDateTime

class ReservationServiceTest extends Specification {

    ReservationService reservationService
    ReservationRepository reservationRepository = Mock(ReservationRepository)
    UserService userService = Mock(UserService)
    ModelMapper mapper = Mock(ModelMapper)
    ReservationPersistRequest reservationPersistDTO = Mock(ReservationPersistRequest)

    def ID = 1

    def setup() {
        reservationService = new ReservationServiceImpl(reservationRepository, mapper)
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

        expect: reservationService.isDateRangeAvailableForEdit(reservationPersistRequest, reservation) == true
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

        expect: reservationService.isDateRangeAvailableForEdit(reservationPersistRequest, reservation) == false
    }

}

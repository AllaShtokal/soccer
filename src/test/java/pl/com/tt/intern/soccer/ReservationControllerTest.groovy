package pl.com.tt.intern.soccer

import org.springframework.http.HttpStatus
import pl.com.tt.intern.soccer.controller.ReservationController
import pl.com.tt.intern.soccer.dto.ReservationPersistDTO
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationService reservationService = Mock(ReservationService)

    def setup() {
        reservationController = new ReservationController(reservationService)
    }

    def "saveNewReservation should invoke ReservationService.save(ReservationPersistDTO)"() {
        given:
        ReservationPersistDTO reservationPersistDTO = Mock(ReservationPersistDTO)

        when:
        reservationController.saveNewReservation(reservationPersistDTO)

        then:
        with(reservationService) {
            1 * save(reservationPersistDTO)
        }
    }

    def "saveNewReservation should return CREATED status"() {
        given:
        ReservationPersistDTO reservationPersistDTO = Mock(ReservationPersistDTO)
        reservationService.verifyPersistedObject() >> {}

        expect:
        reservationController.saveNewReservation(reservationPersistDTO).getStatusCode().equals(HttpStatus.CREATED)
    }

}

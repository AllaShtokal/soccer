package pl.com.tt.intern.soccer.controller

import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationService reservationService = Mock(ReservationService)
    def ID = 1;

    def setup() {
        reservationController = new ReservationController(reservationService)
    }

    def "deleteReservation should invoke ReservationService.deleteById"() {
        given:
        reservationService.existsById(ID) >> true

        when:
        reservationController.deleteReservation(ID)

        then:
        with(reservationService) {
            1 * deleteById(ID)
        }
    }


}

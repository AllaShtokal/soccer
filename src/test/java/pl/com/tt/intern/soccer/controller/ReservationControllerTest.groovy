package pl.com.tt.intern.soccer.controller

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.ReservationService;
import spock.lang.Specification;

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationService reservationService = Mock(ReservationService)
    ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
    def ID = 1

    def setup() {
        reservationController = new ReservationController(reservationService)
    }

    def "update method should be invoked when reservation exists"() {
        given: reservationService.existsByIdAndByUserId(_,_) >> true
        when: reservationController.editOwnReservation(Mock(UserPrincipal), ID , reservationPersistRequest)
        then:
        with(reservationService) {
            1 * update(ID, reservationPersistRequest)
        }
    }

    def "update method should throw exception when reservation does not exist"() {
        given: reservationService.existsByIdAndByUserId(_,_) >> false
        when: reservationController.editOwnReservation(Mock(UserPrincipal), ID , reservationPersistRequest)
        then: thrown(NotFoundException)
    }

}

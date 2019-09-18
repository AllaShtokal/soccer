package pl.com.tt.intern.soccer.controller

import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationService reservationService = Mock(ReservationService)
    def ID = 1
    def userId = 2

    def setup() {
        reservationController = new ReservationController(reservationService)
    }

    def "deleteReservation should invoke ReservationService.deleteById"() {
        given:
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            reservationService.existsByIdAndByUserId(ID, userId) >> true
        when:
            reservationController.deleteOwnReservation(user, ID)
        then:
            with(reservationService) {
                1 * deleteById(ID)
            }
    }


}

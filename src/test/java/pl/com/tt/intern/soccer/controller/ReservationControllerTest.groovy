package pl.com.tt.intern.soccer.controller

import org.springframework.http.HttpStatus
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationService reservationService = Mock()
    def userId = 1

    def setup() {
        reservationController = new ReservationController(reservationService)
    }

    def "saveNewReservationWithOwnId should invoke verification and save"() {
        given:
            ReservationPersistRequest reservationPersistRequest = Mock()
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            reservationService.verifyPersistedObject() >> {}
        when:
            reservationController.saveNewReservationWithOwnId(user, reservationPersistRequest)
        then:
            with(reservationService) {
                1 * verifyPersistedObject(reservationPersistRequest)
                1 * save(reservationPersistRequest, userId)
            }
    }

    def "saveNewReservation should return CREATED status"() {
        given:
            ReservationPersistRequest reservationPersistRequest = Mock()
            UserPrincipal user = Mock(UserPrincipal)
            user.getId() >> userId
            reservationService.verifyPersistedObject() >> {}
        expect:
            reservationController.saveNewReservationWithOwnId(user, reservationPersistRequest).getStatusCode() == HttpStatus.CREATED
    }

}

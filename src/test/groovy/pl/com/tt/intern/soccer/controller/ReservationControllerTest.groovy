package pl.com.tt.intern.soccer.controller

import org.springframework.http.HttpStatus
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.security.UserPrincipal
import pl.com.tt.intern.soccer.service.ReservationService
import spock.lang.Specification

class ReservationControllerTest extends Specification {

    ReservationController reservationController
    ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
    ReservationService reservationService = Mock()
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

    def "update method should be invoked when reservation exists"() {
        given:
            reservationService.existsByIdAndByUserId(_ as Long,_ as Long) >> true
        when:
            reservationController.editOwnReservation(Mock(UserPrincipal), ID , reservationPersistRequest)
        then:
            with(reservationService) {
                1 * update(ID, reservationPersistRequest)
            }
    }

    def "update method should throw exception when reservation does not exist"() {
        given:
            reservationService.existsByIdAndByUserId(_ as Long, _ as Long) >> false
        when:
            reservationController.editOwnReservation(Mock(UserPrincipal), ID, reservationPersistRequest)
        then:
            thrown(NotFoundException)
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

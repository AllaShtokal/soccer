package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

class ReservationServiceTest extends Specification {

    ReservationService reservationService
    ReservationRepository reservationRepository = Mock(ReservationRepository)
    UserService userService = Mock(UserService)
    ModelMapper mapper = Mock(ModelMapper)

    def setup() {
        reservationService = new ReservationServiceImpl(reservationRepository, userService, mapper)
    }

    def "kek"() {
        expect:
        1==1
    }

}

package pl.com.tt.intern.soccer.service

import org.modelmapper.ModelMapper
import pl.com.tt.intern.soccer.exception.NotFoundException
import pl.com.tt.intern.soccer.exception.ReservationClashException
import pl.com.tt.intern.soccer.exception.ReservationFormatException
import pl.com.tt.intern.soccer.model.Lobby
import pl.com.tt.intern.soccer.model.Reservation
import pl.com.tt.intern.soccer.model.User
import pl.com.tt.intern.soccer.model.UserReservationEvent
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest
import pl.com.tt.intern.soccer.payload.request.ReservationSimpleDateRequest
import pl.com.tt.intern.soccer.payload.response.ButtleResponse
import pl.com.tt.intern.soccer.payload.response.GameResponse
import pl.com.tt.intern.soccer.payload.response.ReservationResponse
import pl.com.tt.intern.soccer.payload.response.ReservationShortInfoResponse
import pl.com.tt.intern.soccer.repository.LobbyRepository
import pl.com.tt.intern.soccer.repository.ReservationRepository
import pl.com.tt.intern.soccer.service.impl.ReservationServiceImpl
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.Month

import static java.time.DayOfWeek.MONDAY
import static java.time.LocalDateTime.now
import static java.util.Arrays.asList
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.TODAY

class ReservationServiceTest extends Specification {

    ReservationRepository repository = Mock()
    UserService userService = Mock()
    GameService gameService = Mock()
    ButtleService buttleService = Mock()
    ModelMapper mapper = Mock()
    LobbyRepository lobbyRepository = Mock()
    ReservationPersistRequest reservationPersistRequest = Mock()
    ConfirmationReservationService confirmationService = Mock()

    ReservationService service
    ReservationResponse response
    ReservationShortInfoResponse reservationShortInfoResponse
    Reservation reservationMock
    Reservation reservation
    Lobby lobby
    User user
    UserReservationEvent userReservationEvent


    def ID = 1
    static LocalDateTime timeNow = LocalDateTime.now()

    def setup() {
        service = new ReservationServiceImpl(repository, userService, lobbyRepository, mapper,
                confirmationService, gameService, buttleService)
        response = Mock()
        reservationShortInfoResponse = Mock()
        reservationMock = Mock()

        lobby = new Lobby()
        lobby.setId(10L)
        lobby.setName("ROOM")
        lobby.setLimit(10)
        user = new User()
        user.setId(10L)
        user.setUsername('user')
        userReservationEvent = new UserReservationEvent()
        userReservationEvent.setUser(user)
        user.addUserReservationEvent(userReservationEvent)

        reservation = new Reservation()
        reservation.setId(10L)
        reservation.setUser(user)
        reservation.setUserReservationEvents(asList(userReservationEvent).toSet())
        reservation.setLobby(lobby)
        reservation.setDateFrom(now())
        reservation.setDateTo(now())
        reservation.setConfirmed(_ as Boolean)
    }

    def "deleteById should invoke ReservationRepository.deleteById"() {
        when:
        service.deleteById(ID)
        then:
        1 * repository.deleteById(ID)
    }


    def "isDateRangeAvailable should return true if there are no date collisions"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.now()
        LocalDateTime timeTo = LocalDateTime.now().plusDays(1)
        Reservation reservation = Mock(Reservation)

        ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
        when:
        boolean collides = service.datesCollideWithExistingReservationsExcludingEditedOne(reservationPersistRequest, reservation)
        then:
        collides == true
        1 * reservation.getId() >> ID
        1 * reservationPersistRequest.getDateFrom() >> timeFrom
        1 * reservationPersistRequest.getDateTo() >> timeTo
        1 * repository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> true
    }

    def "isDateRangeAvailable should return false if there are date collisions"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.now()
        LocalDateTime timeTo = LocalDateTime.now().plusDays(1)
        Reservation reservation = Mock(Reservation)
        ReservationPersistRequest reservationPersistRequest = Mock(ReservationPersistRequest)
        when:
        boolean collides = service.datesCollideWithExistingReservationsExcludingEditedOne(reservationPersistRequest, reservation)
        then:
        collides == false
        1 * reservation.getId() >> ID
        1 * reservationPersistRequest.getDateFrom() >> timeFrom
        1 * reservationPersistRequest.getDateTo() >> timeTo
        1 * repository.datesCollideExcludingCurrent(timeFrom, timeTo, ID) >> false
    }

    def "isDateRangeAvailable should return false if there is any date collision"() {
        given:
        LocalDateTime time1 = LocalDateTime.now()
        LocalDateTime time2 = LocalDateTime.now().plusDays(1)
        when:
        boolean collides = service.datesCollideWithExistingReservations(time1, time2)
        then:
        1 * repository.datesCollide(time1, time2) >> true
        collides == true
    }

    def "isDate15MinuteRounded should consider minutes, seconds and nanoseconds"() {
        given:
        LocalDateTime time = LocalDateTime.of(2019, Month.JULY, 1, hour, min, sec, nano)
        expect:
        service.isDate15MinuteRounded(time) == expected
        where:
        hour | min | sec | nano | expected
        0    | 0   | 0   | 0    | true
        12   | 0   | 0   | 0    | true
        23   | 15  | 0   | 0    | true
        7    | 30  | 0   | 0    | true
        7    | 45  | 0   | 0    | true
        13   | 1   | 0   | 0    | false
        14   | 2   | 0   | 0    | false
        15   | 3   | 0   | 0    | false
        16   | 5   | 0   | 0    | false
        17   | 6   | 0   | 0    | false
        18   | 7   | 0   | 0    | false
        19   | 8   | 0   | 0    | false
        20   | 9   | 0   | 0    | false
        21   | 10  | 0   | 0    | false
        22   | 12  | 0   | 0    | false
        8    | 15  | 1   | 0    | false
        9    | 15  | 0   | 1    | false
    }

    def "isDateOrderOk should only return true if dateFrom is smaller than dateTo"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(2019, Month.JULY, day1, hour1, min1, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(2019, Month.JULY, day2, hour2, min2, 0, 0)
        when:
        def result = service.isDateOrderOk(reservationPersistRequest)
        then:
        result == expected
        1 * reservationPersistRequest.getDateFrom() >> timeFrom
        1 * reservationPersistRequest.getDateTo() >> timeTo
        where:
        day1 | hour1 | min1 | day2 | hour2 | min2 | expected
        15   | 12    | 30   | 15   | 12    | 45   | true
        15   | 12    | 30   | 15   | 12    | 30   | false
        15   | 12    | 30   | 14   | 12    | 30   | false
    }

    def "isInFuture should return true if persisted dto has future date"() {
        given:
        reservationPersistRequest.getDateFrom() >> LocalDateTime.now().plusDays(1L)
        expect:
        service.isInFuture(reservationPersistRequest)
    }

    def "isInFuture should return false if persisted dto has past date"() {
        given:
        reservationPersistRequest.getDateFrom() >> LocalDateTime.now().minusDays(1L)
        expect:
        !service.isInFuture(reservationPersistRequest)
    }

    def "'findAll' method should return list of ReservationResponse"() {
        given:
        def list = asList(reservationMock)
        when:
        def result = service.findAll()
        then:
        1 * repository.findAll() >> list
        1 * mapper.map(reservationMock, ReservationResponse) >> response
        result.size() == 1
        result.get(0) == response
    }

    def "'findById' method should return ReservationResponse if found"() {
        when:
        def result = service.findById(ID)
        then:
        1 * repository.findById(ID) >> Optional.of(reservationMock)
        1 * mapper.map(reservationMock, ReservationResponse) >> response
        result == response
    }

    def "'findById' method should throw a NotFoundException if not found"() {
        when:
        service.findById(ID)
        then:
        1 * repository.findById(ID) >> Optional.empty()
        thrown(NotFoundException)
    }

    def "'save' method should return ReservationResponse after save"() {
        when:
        def result = service.save(reservationMock)
        then:
        1 * repository.save(reservationMock) >> reservationMock
        1 * mapper.map(reservationMock, ReservationResponse) >> response
        result == response
    }

    def "'findByDateBetween' method should return a list of ReservationResponse"() {
        given:
        ReservationDateRequest request = Mock()
        def list = asList(reservationMock)
        def responses = asList(response)
        when:
        def result = service.findByDateBetween(request)
        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(request.getFrom(), request.getTo()) >> list
        mapper.map(reservationMock, ReservationResponse) >> response
        result == responses
    }

    def "'findByPeriod' method should return a list of ReservationResponse"() {
        given:
        def period = TODAY
        def list = asList(reservationMock)
        def responses = asList(response)
        when:
        def result = service.findByPeriod(period)
        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(period.from(), period.to()) >> list
        mapper.map(reservationMock, ReservationResponse) >> response
        result == responses
    }

    def "'findShortByPeriod' method should return a list of ReservationShortInfoResponse"() {
        given:
        def period = new ReservationSimpleDateRequest()
        period.setFrom(now())
        period.setTo(now().plusMinutes(30))
        def userId = 10L

        def list = asList(reservation)

        when:
        def result = service.findShortByPeriod(period, userId)
        then:
        1 * repository.findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(_, _) >> list
        mapper.map(reservationMock, ReservationShortInfoResponse) >> response
        result.size() == 1
    }

    def "'findByCreatorId' method should return a list of MyReservationResponse"() {
        given:
        def period = new ReservationSimpleDateRequest()
        period.setFrom(now())
        period.setTo(now().plusMinutes(30))
        def userId = 10L

        def list = asList(reservation)

        when:
        def result = service.findByCreatorId(userId)
        then:
        1 * repository.findAllByUser_Id(userId) >> list
        mapper.map(reservationMock, ReservationShortInfoResponse) >> response
        result.size() == 1
    }

    def "'changeConfirmationReservationStatus' method should change status reservation"() {
        given:
        def confirmed = true

        when:
        service.changeConfirmationReservationStatus(reservationMock, confirmed)
        then:
        1 * repository.save(reservationMock)

    }

    def "'findByDay' method should return a list of ReservationResponse"() {
        given:
        def day = MONDAY
        def list = asList(reservationMock)
        def responses = asList(response)
        def from = now().with(day)
                .withHour(0).withMinute(0).withSecond(0).withNano(0)
        def to = now().with(day)
                .withHour(23).withMinute(59).withSecond(59).withNano(0)
        when:
        def result = service.findByDay(day)
        then:
        1 * repository.findAllByDateToAfterAndDateFromBefore(from, to) >> list
        mapper.map(reservationMock, ReservationResponse) >> response
        result == responses
    }

    def "update should throw NotFoundException if reservation was not found"() {
        given:
        repository.findById(ID) >> Optional.empty()
        when:
        service.update(ID, reservationPersistRequest)
        then:
        thrown(NotFoundException)
    }

    def "update should throw ReservationFormatException if contains invalid date format"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(year1, Month.MAY, day1, hour1, min1, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(year2, Month.MAY, day2, hour2, min2, 0, 0)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.update(ID, reservationPersistRequest)
        then:
        1 * repository.findById(ID) >> Optional.of(reservationMock)
        thrown(ReservationFormatException)
        where:
        year1            | day1 | hour1 | min1 | year2            | day2 | hour2 | min2
        timeNow.year - 1 | 15   | 12    | 30   | timeNow.year - 1 | 15   | 12    | 15 // no future
        timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1 | 15   | 12    | 44 //no round 15 min
        timeNow.year + 1 | 15   | 12    | 31   | timeNow.year + 1 | 15   | 12    | 45 //no round 15 min
        timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1 | 15   | 12    | 15 //wrong order
    }

    def "update should throw ReservationClashException if reservation date range is already booked"() {
        given:
        def reservationID = 540293812
        LocalDateTime timeFrom = LocalDateTime.of(timeNow.year + 1, Month.MAY, 11, 2, 15, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(timeNow.year + 1, Month.MAY, 11, 2, 30, 0, 0)
        reservationMock.getId() >> reservationID
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.update(ID, reservationPersistRequest)
        then:
        1 * repository.findById(ID) >> Optional.of(reservationMock)
        1 * repository.datesCollideExcludingCurrent(timeFrom, timeTo, reservationID) >> true
        thrown(ReservationClashException)
    }

    def "update should invoke repository.save method if reservation validation passes"() {
        given:
        def reservationID = 540293812
        LocalDateTime timeFrom = LocalDateTime.of(timeNow.year + 1, Month.MAY, 11, 2, 15, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(timeNow.year + 1, Month.MAY, 11, 2, 30, 0, 0)
        reservationMock.getId() >> reservationID
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.update(ID, reservationPersistRequest)
        then:
        1 * repository.findById(ID) >> Optional.of(reservationMock)
        1 * repository.datesCollideExcludingCurrent(timeFrom, timeTo, reservationID) >> false
        1 * repository.save(_ as Reservation)
    }

    def "existsById should invoke repository->existsById"() {
        when:
        service.existsById(ID)
        then:
        1 * repository.existsById(ID)
    }

    def "existsByIdAndByUserId should invoke repository->existsByIdAndByUserId"() {
        given:
        def userID = 95133151
        when:
        service.existsByIdAndByUserId(ID, userID)
        then:
        1 * repository.existsByIdAndUserId(ID, userID)
    }

    def "save(ReservationPersistRequest, userID) should invoke repository->save(Reservation)"() {
        given:
        def userID = 1212121
        mapper.map(reservationPersistRequest, Reservation) >> reservationMock
        when:
        service.save(reservationPersistRequest, userID)
        then:
        1 * repository.save(reservationMock)

    }

    def "verifyPersistedObject should throw ReservationFormatException"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(year1, Month.MAY, day1, hour1, min1, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(year2, Month.MAY, day2, hour2, min2, 0, 0)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.verifyPersistedObject(reservationPersistRequest)
        then:
        thrown(ReservationFormatException)
        where:
        year1            | day1 | hour1 | min1 | year2            | day2 | hour2 | min2
        timeNow.year - 1 | 15   | 12    | 30   | timeNow.year - 1 | 15   | 12    | 15 // no future
        timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1 | 15   | 12    | 44 //no round 15 min
        timeNow.year + 1 | 15   | 12    | 31   | timeNow.year + 1 | 15   | 12    | 45 //no round 15 min
        timeNow.year + 1 | 15   | 12    | 30   | timeNow.year + 1 | 15   | 12    | 15 //wrong order
    }

    def "verifyPersistedObject should throw ReservationClashException"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(timeNow.year + 1, Month.MAY, 12, 3, 15, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(timeNow.year + 2, Month.MAY, 12, 4, 30, 0, 0)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.verifyPersistedObject(reservationPersistRequest)
        then:
        1 * repository.datesCollide(timeFrom, timeTo) >> true
        thrown(ReservationClashException)
    }

    def "verifyPersistedObject should not throw any exception"() {
        given:
        LocalDateTime timeFrom = LocalDateTime.of(timeNow.year + 1, Month.MAY, 12, 3, 15, 0, 0)
        LocalDateTime timeTo = LocalDateTime.of(timeNow.year + 2, Month.MAY, 12, 4, 30, 0, 0)
        reservationPersistRequest.getDateFrom() >> timeFrom
        reservationPersistRequest.getDateTo() >> timeTo
        when:
        service.verifyPersistedObject(reservationPersistRequest)
        then:
        1 * repository.datesCollide(timeFrom, timeTo) >> false
        noExceptionThrown()
    }

    def "'getWinnerTeamByMatch' method should return TeamResponse"() {
        given:
        def matchId = 10L
        def buttles = new ButtleResponse()
        List<ButtleResponse> buttlesList = asList(buttles)
        def gameResponse = new GameResponse()
        gameResponse.setButtles(buttlesList)

        when:
        def result = service.getWinnerTeamByMatch(matchId)
        then:
        1 * gameService.getlastGameInMatch(matchId) >> gameResponse
        1 * buttleService.getTeamWinner(_)
    }
}

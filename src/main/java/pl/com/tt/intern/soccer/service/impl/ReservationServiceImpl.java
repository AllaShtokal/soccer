package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.exception.*;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Lobby;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.request.ReservationSimpleDateRequest;
import pl.com.tt.intern.soccer.payload.response.*;
import pl.com.tt.intern.soccer.repository.LobbyRepository;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private static final Integer TIME_ROUNDING_IN_MINUTES = 15;
    private static final String NOT_ROUNDED_MESSAGE = String.format("Date must be rounded to %s minutes 0 s 0 ns", TIME_ROUNDING_IN_MINUTES);
    private static final String RESERVATION_ALREADY_BOOKED_MESSAGE = "Reservation date range is already booked";
    private static final String WRONG_DATE_ORDER_MESSAGE = "Wrong date order";
    private static final String DATE_MUST_BE_FUTURE_MESSAGE = "Date must be in future";


    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final LobbyRepository lobbyRepository;
    private final ModelMapper mapper;
    private final ConfirmationReservationService confirmationService;
    private final GameService gameService;
    private final ButtleService buttleService;


    @Override
    public List<ReservationResponse> findAll() {
        log.debug("Finding all reservations...");
        return mapToResponse(reservationRepository.findAll());
    }

    @Transactional
    @Override
    public ReservationResponse findById(Long id) throws NotFoundException {
        log.debug("Finding reservation by id: {}", id);
        return mapToResponse(reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new));
    }

    @Transactional
    @Override
    public ReservationResponse save(Reservation reservation) {
        ReservationResponse response = mapToResponse(reservationRepository.save(reservation));
        log.debug("Saving a new reservation: {}", response);
        return response;
    }

    @Override
    @Transactional
    public ReservationPersistedResponse save(ReservationPersistRequest reservationPersistRequest, Long userId) throws NotFoundException {
        Reservation reservation = mapper.map(reservationPersistRequest, Reservation.class);
        reservation.setConfirmed(false);
        reservation.setId(null);
        Lobby lobby = lobbyRepository.findFirstByName(reservationPersistRequest.
                getLobbyName()).orElseThrow(() -> new NotFoundLobbyByIdException(reservationPersistRequest.getLobbyName()));
        reservation.setLobby(lobby);
        User user = userService.findById(userId);
        reservation.setUser(user);
        reservation.setConfirmed(true);
        Reservation savedEntity = reservationRepository.save(reservation);
        confirmationService.createAndSaveConfirmationReservation(savedEntity);
        return mapper.map(savedEntity, ReservationPersistedResponse.class);
    }


    @Override
    @Transactional
    public void deleteById(Long id) {
        log.debug("Deleting a reservation with id: {}", id);
        reservationRepository.deleteById(id);
    }

    @Override
    public List<ReservationResponse> findByDateBetween(ReservationDateRequest request) {
        log.debug("Finding a reservation by date between {} and {}", request.getFrom(), request.getTo());
        return mapToResponse(reservationRepository.findAllByDateToAfterAndDateFromBefore(
                request.getFrom(), request.getTo()));
    }

    @Override
    public List<ReservationResponse> findByPeriod(ReservationPeriod period) {
        log.debug("Finding all reservations in period: {}", period);
        return mapToResponse(reservationRepository.findAllByDateToAfterAndDateFromBefore(period.from(), period.to()));
    }

    @Override
    public List<ReservationShortInfoResponse> findShortByPeriod(ReservationSimpleDateRequest period, Long userId) {
        log.debug("Finding all reservations in period: {}", period);

        return mapToReservationShortInfoResponse(reservationRepository.findAllByDateFromGreaterThanEqualAndDateToLessThanEqual(period.getFrom(), period.getTo()), userId);
    }

    @Override
    public List<MyReservationResponse> findByCreatorId(Long userId) {
        log.debug("Finding all created by this user: {}", userId);
        List<Reservation> allByUserId = reservationRepository.findAllByUser_Id(userId);
        return mapToMyReservationResponse(allByUserId, userId);
    }

    private List<MyReservationResponse> mapToMyReservationResponse(List<Reservation> reservations, Long userId) {
        List<MyReservationResponse> responseList = new ArrayList<>();
        for (Reservation r : reservations) {
            responseList.add(new MyReservationResponse(r, userId));
        }
        return responseList;
    }

    private List<ReservationShortInfoResponse> mapToReservationShortInfoResponse(List<Reservation> reservations, Long userId) {
        List<ReservationShortInfoResponse> responseList = new ArrayList<>();
        for (Reservation r : reservations) {
            responseList.add(new ReservationShortInfoResponse(r, userId));
        }
        return responseList;
    }

    @Override
    public List<ReservationResponse> findByDay(DayOfWeek day) {
        log.debug("Finding a reservations by day: {}", day);
        return mapToResponse(reservationRepository.findAllByDateToAfterAndDateFromBefore(from(day), to(day)));
    }

    private LocalDateTime from(DayOfWeek day) {
        return now().with(day)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime to(DayOfWeek day) {
        return now().with(day)
                .withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

    private List<ReservationResponse> mapToResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::mapToResponse)
                .collect(toList());
    }

    private ReservationResponse mapToResponse(Reservation reservation) {
        return mapper.map(reservation, ReservationResponse.class);
    }

    @Override
    public void confirmReservationByConfirmationKey(String confirmationKey) throws IncorrectConfirmationKeyException {
        ConfirmationReservation confirmationReservation = confirmationService.findConfirmationReservationByUUID(confirmationKey);

        checkIfExpired(confirmationReservation.getExpirationTime());
        confirmationReservation.setExpirationTime(now());
        changeConfirmationReservationStatus(confirmationReservation.getReservation(), true);
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws IncorrectConfirmationKeyException {
        if (!expirationTimeToken.isAfter(now().plusHours(2)))
            throw new IncorrectConfirmationKeyException("The token has expired.");
    }

    @Override
    public void changeConfirmationReservationStatus(Reservation reservation, Boolean confirmed) {
        reservation.setConfirmed(confirmed);
        reservationRepository.save(reservation);
    }

    @Override
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }

    @Override
    public boolean existsByIdAndByUserId(Long reservationId, Long userId) {
        return reservationRepository.existsByIdAndUserId(reservationId, userId);
    }

    @Override
    public ReservationPersistedResponse update(Long reservationId, ReservationPersistRequest requestObject) throws NotFoundException, ReservationClashException, ReservationFormatException {
        Optional<Reservation> reservationById = reservationRepository.findById(reservationId);
        Reservation reservation = reservationById.orElseThrow(NotFoundException::new);
        verifyEditedReservation(requestObject, reservation);
        reservation.setDateFrom(requestObject.getDateFrom());
        reservation.setDateTo(requestObject.getDateTo());
        Lobby lobby= lobbyRepository.findFirstByName(requestObject.getLobbyName())
                .orElseThrow(() -> new NotFoundLobbyByIdException(requestObject.getLobbyName()));
        reservation.setLobby(lobby);
        Reservation savedReservation = reservationRepository.save(reservation);
        return mapper.map(savedReservation, ReservationPersistedResponse.class);
    }

    public void verifyEditedReservation(ReservationPersistRequest reservationPersistRequest, Reservation currentReservation) throws ReservationFormatException, ReservationClashException {
        if (!isInFuture(reservationPersistRequest))
            throw new ReservationFormatException(DATE_MUST_BE_FUTURE_MESSAGE);
        if (!isDateOrderOk(reservationPersistRequest))
            throw new ReservationFormatException(WRONG_DATE_ORDER_MESSAGE);
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateFrom()))
            throw new ReservationFormatException(NOT_ROUNDED_MESSAGE);
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateTo()))
            throw new ReservationFormatException(NOT_ROUNDED_MESSAGE);
        if (datesCollideWithExistingReservationsExcludingEditedOne(reservationPersistRequest, currentReservation))
            throw new ReservationClashException(RESERVATION_ALREADY_BOOKED_MESSAGE);
    }

    public void verifyPersistedObject(ReservationPersistRequest reservationPersistRequest) throws ReservationFormatException, ReservationClashException {
        if (!isInFuture(reservationPersistRequest))
            throw new ReservationFormatException(DATE_MUST_BE_FUTURE_MESSAGE);
        if (!isDateOrderOk(reservationPersistRequest))
            throw new ReservationFormatException(WRONG_DATE_ORDER_MESSAGE);
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateFrom()))
            throw new ReservationFormatException(NOT_ROUNDED_MESSAGE);
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateTo()))
            throw new ReservationFormatException(NOT_ROUNDED_MESSAGE);
        if (datesCollideWithExistingReservations(reservationPersistRequest.getDateFrom(), reservationPersistRequest.getDateTo()))
            throw new ReservationClashException(RESERVATION_ALREADY_BOOKED_MESSAGE);
    }

    public boolean datesCollideWithExistingReservationsExcludingEditedOne(ReservationPersistRequest reservationPersistRequest, Reservation currentReservation) {
        return reservationRepository.datesCollideExcludingCurrent(reservationPersistRequest.getDateFrom(),
                reservationPersistRequest.getDateTo(),
                currentReservation.getId());
    }

    @Override
    public boolean isInFuture(ReservationPersistRequest reservationPersistDTO) {
        return reservationPersistDTO.getDateFrom().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isDateOrderOk(ReservationPersistRequest reservationPersistRequest) {
        return reservationPersistRequest.getDateFrom().isBefore(reservationPersistRequest.getDateTo());
    }

    @Override
    public boolean isDate15MinuteRounded(LocalDateTime time) {
        if (time.getNano() != 0) return false;
        if (time.getSecond() != 0) return false;
        return time.getMinute() % TIME_ROUNDING_IN_MINUTES == 0;
    }

    @Override
    public TeamResponse getWinnerTeamByMatch(Long match_id) throws NotFoundException {

        GameResponse gameResponse = gameService.getlastGameInMatch(match_id);
        List<ButtleResponse> buttles = gameResponse.getButtles();
        return buttleService.getTeamWinner(buttles.get(0));


    }

    @Override
    public boolean datesCollideWithExistingReservations(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return reservationRepository.datesCollide(dateFrom, dateTo);
    }
}

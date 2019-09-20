package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.exception.*;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForConfirmationReservation;
import pl.com.tt.intern.soccer.model.ConfirmationReservation;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyForConfirmationReservationService;
import pl.com.tt.intern.soccer.service.ConfirmationReservationService;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConfirmationKeyForConfirmationReservationService confirmationKeyService;
    private final ConfirmationReservationService confirmationReservationService;
    private final UserService userService;
    private final ModelMapper mapper;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) throws NotFoundException {
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public ReservationPersistedResponse save(ReservationPersistRequest reservationPersistRequest, Long userId) throws NotFoundException {
        Reservation reservation = mapper.map(reservationPersistRequest, Reservation.class);
        reservation.setConfirmed(false);
        reservation.setId(null);
        reservation.setUser(userService.findById(userId));
        Reservation savedEntity = reservationRepository.save(reservation);
        confirmationReservationService.saveAndAddConfirmationReservationToTaskTimer(new ConfirmationReservation(reservation));
        return mapper.map(savedEntity, ReservationPersistedResponse.class);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
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
        Reservation savedReservation = reservationRepository.save(reservation);
        return mapper.map(savedReservation, ReservationPersistedResponse.class);
    }

    public void verifyEditedReservation(ReservationPersistRequest reservationPersistRequest, Reservation currentReservation) throws ReservationFormatException, ReservationClashException {
        if (!isInFuture(reservationPersistRequest))
            throw new ReservationFormatException("Date must be in future");
        if (!isDateOrderOk(reservationPersistRequest))
            throw new ReservationFormatException("Wrong date order");
       /* if (!isDate15MinuteRounded(reservationPersistRequest.getDateFrom()))
            throw new ReservationFormatException("Date must be rounded to 15 minutes 0 s 0 ns");
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateTo()))
            throw new ReservationFormatException("Date must be rounded to 15 minutes 0 s 0 ns");*/
        if (!isDateRangeAvailableForEdit(reservationPersistRequest, currentReservation))
            throw new ReservationClashException("Reservation date range is already booked");
    }

    public void verifyPersistedObject(ReservationPersistRequest reservationPersistRequest) throws ReservationFormatException, ReservationClashException {
        if (!isInFuture(reservationPersistRequest))
            throw new ReservationFormatException("Date must be in future");
        if (!isDateOrderOk(reservationPersistRequest))
            throw new ReservationFormatException("Wrong date order");
       /* if (!isDate15MinuteRounded(reservationPersistRequest.getDateFrom()))
            throw new ReservationFormatException("Date must be rounded to 15 minutes 0 s 0 ns");
        if (!isDate15MinuteRounded(reservationPersistRequest.getDateTo()))
            throw new ReservationFormatException("Date must be rounded to 15 minutes 0 s 0 ns");*/
        if (!isDateRangeAvailable(reservationPersistRequest.getDateFrom(), reservationPersistRequest.getDateTo()))
            throw new ReservationClashException("Reservation date range is already booked");
    }

    public boolean isDateRangeAvailableForEdit(ReservationPersistRequest reservationPersistRequest, Reservation currentReservation) {
        return !reservationRepository.datesCollideExcludingCurrent(reservationPersistRequest.getDateFrom(),
                reservationPersistRequest.getDateTo(),
                currentReservation.getId());
    }

    @Override
    public boolean isInFuture(ReservationPersistRequest reservationPersistDTO) {
        return reservationPersistDTO.getDateFrom().isAfter(now());
    }

    @Override
    public boolean isDateOrderOk(ReservationPersistRequest reservationPersistRequest) {
        return reservationPersistRequest.getDateFrom().isBefore(reservationPersistRequest.getDateTo());
    }

    @Override
    public boolean isDate15MinuteRounded(LocalDateTime time) {
        if (time.getNano() != 0) return false;
        if (time.getSecond() != 0) return false;
        if (time.getMinute() % 15 != 0) return false;
        return true;
    }

    @Override
    public boolean isDateRangeAvailable(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return !reservationRepository.datesCollide(dateFrom, dateTo);
    }

    @Override
    public void confirmReservationByToken(String uuid) throws ConfirmationReservationException {
        try {
            ConfirmationKeyForConfirmationReservation reservationKey = confirmationKeyService.findConfirmationKeyByUuid(uuid);
            checkIfExpired(reservationKey.getExpirationTime());
            reservationKey.setExpirationTime(now());
            changeConfirmationOfReservation(reservationKey.getReservation(), true);
        } catch (NotFoundException e) {
            throw new ConfirmationReservationException("The account activation token can't be found in the database.");
        }
    }

    private void checkIfExpired(LocalDateTime expirationTimeToken) throws ConfirmationReservationException {
        if (!expirationTimeToken.isAfter(now()))
            throw new ConfirmationReservationException("The confirmation reservation token has expired.");
    }

    @Override
    public void changeConfirmationOfReservation(Reservation reservation, Boolean confirmed) {
        reservation.setConfirmed(confirmed);
        reservationRepository.save(reservation);
    }
}

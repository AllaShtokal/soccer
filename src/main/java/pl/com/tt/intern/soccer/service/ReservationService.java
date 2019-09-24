package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationClashException;
import pl.com.tt.intern.soccer.exception.ReservationFormatException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<ReservationResponse> findAll();

    ReservationResponse findById(Long id) throws NotFoundException;

    ReservationResponse save(Reservation reservation);

    ReservationPersistedResponse save(ReservationPersistRequest reservation, Long userId) throws NotFoundException;

    boolean datesCollideWithExistingReservations(LocalDateTime dateFrom, LocalDateTime dateTo) throws ReservationFormatException;

    void deleteById(Long id);

    List<ReservationResponse> findByDateBetween(ReservationDateRequest request);

    List<ReservationResponse> findByPeriod(ReservationPeriod period);

    List<ReservationResponse> findByDay(DayOfWeek day);

    boolean existsById(Long id);

    boolean existsByIdAndByUserId(Long reservationId, Long userId);

    ReservationPersistedResponse update(Long id, ReservationPersistRequest request) throws NotFoundException, ReservationClashException, ReservationFormatException;

    boolean datesCollideWithExistingReservationsExcludingEditedOne(ReservationPersistRequest reservationPersistRequest,
                                                                   Reservation currentReservation);

    void verifyPersistedObject(ReservationPersistRequest reservationPersistRequest) throws ReservationFormatException, ReservationClashException;

    boolean isInFuture(ReservationPersistRequest reservationPersistRequest);

    boolean isDateOrderOk(ReservationPersistRequest reservationPersistRequest);

    boolean isDate15MinuteRounded(LocalDateTime time);
}

package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.ReservationClashException;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationClashException;
import pl.com.tt.intern.soccer.exception.ReservationFormatException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    ReservationPersistedResponse save(ReservationPersistRequest reservation, Long userId) throws NotFoundException;

    boolean isDateRangeAvailable(LocalDateTime dateFrom, LocalDateTime dateTo) throws ReservationFormatException;

    void deleteById(Long id);

    boolean existsByIdAndByUserId(Long reservationId, Long userId);

    ReservationPersistedResponse update(Long id, ReservationPersistRequest request) throws NotFoundException, ReservationClashException, ReservationFormatException;

    boolean isDateRangeAvailableForEdit(ReservationPersistRequest reservationPersistRequest,
                                                Reservation currentReservation);

    void verifyPersistedObject(ReservationPersistRequest reservationPersistRequest) throws ReservationFormatException, ReservationClashException;

    boolean isInFuture(ReservationPersistRequest reservationPersistRequest);

    boolean isDateOrderOk(ReservationPersistRequest reservationPersistRequest);

    boolean isDate15MinuteRounded(LocalDateTime time);
}

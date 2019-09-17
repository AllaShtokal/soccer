package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationJustPersistedConfirmationResponse;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationException;
import pl.com.tt.intern.soccer.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    ReservationJustPersistedConfirmationResponse save(ReservationPersistRequest reservation) throws NotFoundException;

    boolean isDateRangeAvailable(ReservationPersistRequest reservationPersistDTO) throws ReservationException;

    void deleteById(Long id);

    void verifyPersistedObject(ReservationPersistRequest reservationPersistDTO) throws ReservationException;

    boolean isInFuture(ReservationPersistRequest reservationPersistDTO);

    boolean isDateOrderOk(ReservationPersistRequest reservationPersistDTO);

    boolean isDate15MinuteRounded(LocalDateTime time);
}

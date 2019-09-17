package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationFormatException;
import pl.com.tt.intern.soccer.model.Reservation;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    ReservationPersistedResponse save(ReservationPersistRequest reservation) throws NotFoundException;

    boolean isDateRangeAvailable(LocalDateTime dateFrom, LocalDateTime dateTo) throws ReservationFormatException;

    void deleteById(Long id);

    void verifyPersistedObject(ReservationPersistRequest reservationPersistRequest) throws ReservationFormatException;

    boolean isInFuture(ReservationPersistRequest reservationPersistRequest);

    boolean isDateOrderOk(ReservationPersistRequest reservationPersistRequest);

    boolean isDate15MinuteRounded(LocalDateTime time);
}

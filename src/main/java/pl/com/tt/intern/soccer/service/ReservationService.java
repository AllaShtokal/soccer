package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.dto.ReservationPersistDTO;
import pl.com.tt.intern.soccer.dto.ReservationRetrieveDTO;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationException;
import pl.com.tt.intern.soccer.model.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    ReservationRetrieveDTO save(ReservationPersistDTO reservation) throws NotFoundException;

    boolean isDateRangeAvailable(ReservationPersistDTO reservationPersistDTO) throws ReservationException;

    void deleteById(Long id);

    void verifyPersistedObject(ReservationPersistDTO reservationPersistDTO) throws ReservationException;
}

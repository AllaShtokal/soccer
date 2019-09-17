package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.payload.request.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationService {

    List<ReservationResponse> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<ReservationResponse> findByDateBetween(LocalDateTime from, LocalDateTime to);

    List<ReservationResponse> findByPeriod(ReservationPeriod period);

}

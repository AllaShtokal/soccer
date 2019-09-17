package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.request.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    List<ReservationResponse> findAll();

    Reservation findById(Long id) throws NotFoundException;

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    List<ReservationResponse> findByDateBetween(ReservationDateRequest request);

    List<ReservationResponse> findByPeriod(ReservationPeriod period);

}

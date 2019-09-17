package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.payload.request.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static pl.com.tt.intern.soccer.payload.request.ReservationPeriod.ALL;
import static pl.com.tt.intern.soccer.util.TimeUtil.from;
import static pl.com.tt.intern.soccer.util.TimeUtil.to;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationResponse> findAll() {
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .collect(toList());
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

    @Transactional
    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public List<ReservationResponse> findByDateBetween(LocalDateTime from, LocalDateTime to) {
        return reservationRepository.findAllByDateToAfterAndDateFromBefore(from, to).stream()
                .map(ReservationResponse::new)
                .collect(toList());
    }

    @Override
    public List<ReservationResponse> findByPeriod(ReservationPeriod period) {
        return period.equals(ALL) ? findAll() : findByDateBetween(from(period), to(period));
    }

}

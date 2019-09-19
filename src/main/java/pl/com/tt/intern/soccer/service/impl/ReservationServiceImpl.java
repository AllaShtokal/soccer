package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.temporal.TemporalAdjusters.nextOrSame;
import static java.time.temporal.TemporalAdjusters.previousOrSame;
import static java.util.stream.Collectors.toList;
import static pl.com.tt.intern.soccer.model.enums.ReservationPeriod.ALL;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<ReservationResponse> findAll() {
        log.debug("Finding all reservations...");
        return reservationRepository.findAll().stream()
                .map(ReservationResponse::new)
                .collect(toList());
    }

    @Override
    public Reservation findById(Long id) throws NotFoundException {
        log.debug("Finding reservation by id: {}", id);
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public ReservationResponse save(Reservation reservation) {
        ReservationResponse response =
                new ReservationResponse(reservationRepository.save(reservation));
        log.debug("Saving a new reservation: {}", response);
        return response;
    }

    @Override
    public void deleteById(Long id) {
        log.debug("Deleting a reservation with id: {}", id);
        reservationRepository.deleteById(id);
    }

    @Override
    public List<ReservationResponse> findByDateBetween(ReservationDateRequest request) {
        log.debug("Finding a reservation by date between {} and {}", request.getFrom(), request.getTo());
        return reservationRepository.findAllByDateToAfterAndDateFromBefore(request.getFrom(), request.getTo()).stream()
                .map(ReservationResponse::new)
                .collect(toList());
    }

    @Override
    public List<ReservationResponse> findByPeriod(ReservationPeriod period) {
        log.debug("Finding all reservations in period: {}", period);
        return period.equals(ALL) ? findAll() : findByDateBetween(new ReservationDateRequest(period.from(), period.to()));
    }

    @Override
    public List<ReservationResponse> findByDay(DayOfWeek day) {
        log.debug("Finding a reservations by day: {}", day);
        return findByDateBetween(new ReservationDateRequest(from(day), to(day)));
    }

    private LocalDateTime from(DayOfWeek day) {
        return now().with(previousOrSame(day))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
    }

    private LocalDateTime to(DayOfWeek day) {
        return now().with(nextOrSame(day))
                .withHour(23).withMinute(59).withSecond(59).withNano(0);
    }

}

package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Slf4j
@PreAuthorize("isAuthenticated()")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(params = "period")
    public ResponseEntity<List<ReservationResponse>> findByPeriod(@RequestParam ReservationPeriod period) {
        log.debug("GET /reservations?period={}", period);
        return ok(reservationService.findByPeriod(period));
    }

    @GetMapping(params = "day")
    public ResponseEntity<List<ReservationResponse>> findByDay(@RequestParam DayOfWeek day) {
        log.debug("GET /reservations?day={}", day);
        return ok(reservationService.findByDay(day));
    }

    @GetMapping("/date")
    public ResponseEntity<List<ReservationResponse>> findByDate(@Valid @RequestBody ReservationDateRequest request) {
        log.debug("GET /reservations/date, body: {}", request);
        return ok(reservationService.findByDateBetween(request));
    }

}

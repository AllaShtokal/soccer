package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.model.enums.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationClashException;
import pl.com.tt.intern.soccer.exception.ReservationFormatException;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.validation.Valid;
import java.time.DayOfWeek;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> findAll() {
        log.debug("GET /reservations");
        return ok(reservationService.findAll());
    }

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

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOwnReservation(@CurrentUser UserPrincipal user, @PathVariable("id") Long id) throws NotFoundException {
        log.debug("DELETE: /reservations/{}", id);
        if (reservationService.existsByIdAndByUserId(id, user.getId())) {
            reservationService.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        throw new NotFoundException("Reservation either does not exist or does not belong to the requesting user.");
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<ReservationPersistedResponse> saveNewReservationWithOwnId(
            @CurrentUser UserPrincipal user,
            @Valid @RequestBody ReservationPersistRequest reservationPersistDTO)
            throws NotFoundException, ReservationFormatException, ReservationClashException {
        log.debug("POST: /reservations with body: {}", reservationPersistDTO);
        reservationService.verifyPersistedObject(reservationPersistDTO);
        return ResponseEntity
                .status(CREATED)
                .body(reservationService.save(reservationPersistDTO, user.getId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<ReservationPersistedResponse> editOwnReservation(
            @CurrentUser UserPrincipal user,
            @PathVariable("id") Long id,
            @Valid @RequestBody ReservationPersistRequest reservationPersistRequest
    ) throws NotFoundException, ReservationClashException, ReservationFormatException {
        if (reservationService.existsByIdAndByUserId(id, user.getId())) {
            return ResponseEntity
                    .ok(reservationService.update(id, reservationPersistRequest));
        }
        throw new NotFoundException("Reservation either does not exist or does not belong to the requesting user.");
    }

}

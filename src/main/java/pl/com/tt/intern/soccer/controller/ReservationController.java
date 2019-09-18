package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

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

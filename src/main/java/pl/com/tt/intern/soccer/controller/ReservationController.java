package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationJustPersistedConfirmationResponse;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationException;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationJustPersistedConfirmationResponse> saveNewReservation(@Valid @RequestBody ReservationPersistRequest reservationPersistDTO) throws NotFoundException, ReservationException {
        log.debug("POST: /reservations with body: {}", reservationPersistDTO);
        reservationService.verifyPersistedObject(reservationPersistDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservationService.save(reservationPersistDTO));
    }
}

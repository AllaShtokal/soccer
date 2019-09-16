package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.dto.ReservationPersistDTO;
import pl.com.tt.intern.soccer.dto.ReservationRetrieveDTO;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping(value="/{id}")
    public ResponseEntity<Reservation> getById(@PathVariable("id") long id) throws NotFoundException {
        log.debug("GET: /reservations/{}", id);
        Reservation reservationById = reservationService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reservationById);
    }

    @PostMapping
    public ResponseEntity<ReservationRetrieveDTO> saveNewReservation(@Valid @RequestBody ReservationPersistDTO reservationPersistDTO) throws NotFoundException, ReservationException {
        log.debug("POST: /reservations with body: {}", reservationPersistDTO);
        if (reservationService.isPersistedObjectOk(reservationPersistDTO)) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(reservationService.save(reservationPersistDTO));
        } else {
            // TODO: 16.09.2019 LOGGER HERE + reservation exception to controller advice
            throw new ReservationException();
        }

    }

}

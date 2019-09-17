package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.service.ReservationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable("id") Long id) throws NotFoundException {
        log.debug("DELETE: /reservations/{}", id);
        if (reservationService.existsById(id)) {
            reservationService.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        throw new NotFoundException(String.format("Could not find user with id=%d - deletion impossible.", id));
    }

}

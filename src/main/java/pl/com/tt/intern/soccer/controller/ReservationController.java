package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.ReservationService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> deleteOwnReservation(@CurrentUser UserPrincipal user,
                                                    @PathVariable("id") Long id)
                                                    throws NotFoundException {
        log.debug("DELETE: /reservations/{}", id);
        if (reservationService.existsByIdAndByUserId(id, user.getId())) {
            reservationService.deleteById(id);
            return ResponseEntity
                    .noContent()
                    .build();
        }
        throw new NotFoundException("Reservation does not exist for this user - cannot be deleted");
    }

}

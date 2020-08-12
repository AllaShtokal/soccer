package pl.com.tt.intern.soccer.controller;

import com.sun.org.apache.xpath.internal.operations.Bool;
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
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.MatchService;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class MatchController {

    private final MatchService matchService;


    @GetMapping("/start/{id}")
    public ResponseEntity<MatchResponseRequest> createNewMatchByReservationId(@PathVariable("id") Long reservation_id) {

        MatchResponseRequest matchResponse = matchService.play(reservation_id);
        return ok(matchResponse);
    }
    //check if only one team is active, if so return

    @PostMapping("/confirm")
    public ResponseEntity<Boolean>  confirmGameResults(
            @Valid @RequestBody MatchResponseRequest matchResponseRequest){
        Boolean isContinued;
        isContinued = matchService.saveResults(matchResponseRequest);
        return ResponseEntity
                .status(OK)
                .body(isContinued);
    }

}

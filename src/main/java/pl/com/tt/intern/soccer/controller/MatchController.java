package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.payload.response.MatchResponse;
import pl.com.tt.intern.soccer.service.MatchService;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/startmatch")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class MatchController {

    MatchService matchService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<MatchResponse> createNewMatchByReservationId(@PathVariable("id") Long reservation_id) {

        return ok(matchService.create(reservation_id));
    }
}

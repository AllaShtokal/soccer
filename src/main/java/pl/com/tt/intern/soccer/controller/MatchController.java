package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.TeamRequest;
import pl.com.tt.intern.soccer.payload.response.MatchFullResponse;
import pl.com.tt.intern.soccer.payload.response.MatchResponseRequest;
import pl.com.tt.intern.soccer.payload.response.MatchResultsResponse;
import pl.com.tt.intern.soccer.service.MatchService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/match")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class MatchController {

    private final MatchService matchService;


    @PatchMapping("/start/{reservation_id}")
    public ResponseEntity<MatchResponseRequest> playByReservationId(@PathVariable("reservation_id") Long reservationId,
                                                                    @RequestBody(required = false) List<TeamRequest> teamRequests) throws Exception {

          MatchResponseRequest matchResponse = matchService.play(reservationId, teamRequests);
        return ok(matchResponse);
    }

    @GetMapping("/results/{match_id}")
    public ResponseEntity<MatchResultsResponse> getMatchResults(@PathVariable("match_id") Long matchId) throws NotFoundException {

        MatchResultsResponse matchResultsResponse = matchService.getMatchResult(matchId);
        return ok(matchResultsResponse);
    }

    @PostMapping("/confirm")
    public ResponseEntity<Boolean>  confirmGameResults(
            @Valid @RequestBody MatchResponseRequest matchResponseRequest) throws NotFoundException {
        Boolean isContinued;
        isContinued = matchService.saveResults(matchResponseRequest);
        return ResponseEntity
                .status(OK)
                .body(isContinued);
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<List<MatchFullResponse>> findAll(@PathVariable("reservation_id") Long reservationId) throws NotFoundException {
        return ok(matchService.findAllByReservationId(reservationId));
    }

}

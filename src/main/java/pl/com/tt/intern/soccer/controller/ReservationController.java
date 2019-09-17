package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.payload.request.ReservationDateRequest;
import pl.com.tt.intern.soccer.payload.request.ReservationPeriod;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.service.ReservationService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReservationResponse>> find(@RequestParam(name = "period") ReservationPeriod period) {
        return ok(reservationService.findByPeriod(period));
    }

    @GetMapping("/check")
    public ResponseEntity<List<ReservationResponse>> find(@Valid @RequestBody ReservationDateRequest request) {
        return ok(reservationService.findByDateBetween(request.getFrom(), request.getTo()));
    }

}

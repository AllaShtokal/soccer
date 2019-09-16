package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.service.ReservationService;

@RestController
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

}

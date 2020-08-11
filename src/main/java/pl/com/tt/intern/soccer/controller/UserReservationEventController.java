package pl.com.tt.intern.soccer.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.service.UserReservationService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/userreservation")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class UserReservationEventController {

    private final ReservationService reservationService;
    private final UserReservationService userReservationService;

    @GetMapping("/add/{id}")
    public ResponseEntity<ReservationResponse> AddUserReservation(@CurrentUser UserPrincipal user,
                                                     @PathVariable("id") Long id) throws NotFoundException {

        userReservationService.add(id, user.getId());
        ReservationResponse reservationResponse = reservationService.findById(id);

        return ok(reservationResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<BasicUserInfoResponse>> FindAllUsersByUserReservationId(
                                                                  @PathVariable("id") Long id) throws NotFoundException {

        List<BasicUserInfoResponse> usersList = userReservationService.findAllUsersByReservationID(id);

        return ok(usersList);
    }
}

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

    @GetMapping("/add/{reservation_id}")
    public ResponseEntity<ReservationResponse> addUserReservation(@CurrentUser UserPrincipal user,
                                                     @PathVariable("reservation_id") Long reservation_id) throws Exception  { //todo correct exception

        userReservationService.add(reservation_id, user.getId());
        ReservationResponse reservationResponse = reservationService.findById(reservation_id);
        return ok(reservationResponse);
    }

    @GetMapping("/{reservation_id}")
    public ResponseEntity<List<BasicUserInfoResponse>> findAllUsersByUserReservationId(
                                                                  @PathVariable("reservation_id") Long reservation_id) throws NotFoundException {

        List<BasicUserInfoResponse> usersList = userReservationService.findAllUsersByReservationID(reservation_id);

        return ok(usersList);
    }

    @GetMapping("/remove/{reservation_id}")
    public ResponseEntity<ReservationResponse> removeUserReservation(@CurrentUser UserPrincipal user,
                                                                  @PathVariable("reservation_id") Long reservation_id) throws NotFoundException {

        userReservationService.remove(reservation_id, user.getId());
        ReservationResponse reservationResponse = reservationService.findById(reservation_id);
        return ok(reservationResponse);
    }


}

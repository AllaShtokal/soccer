package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.repository.UserReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.service.UserReservationService;
import pl.com.tt.intern.soccer.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserReservationServiceImpl implements UserReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userService;
    private final UserReservationRepository userReservationRepository;

    @Transactional
    @Override
    public void add(Long reservation_id, Long user_id) {

        Reservation reservation = reservationRepository.findById(reservation_id).get();
        User user = userService.findById(user_id).get();

        UserReservationEvent userReservationEvent = new UserReservationEvent();
        userReservationEvent.setReservation(reservation);
        userReservationEvent.setUser(user);

        userReservationEvent.setRegisteredAt(LocalDateTime.now());

        //reservation.addUserReservationEvent(userReservationEvent);
        //user.addUserReservationEvent(userReservationEvent);
//
//
//
          userReservationRepository.save(userReservationEvent);

    }
}

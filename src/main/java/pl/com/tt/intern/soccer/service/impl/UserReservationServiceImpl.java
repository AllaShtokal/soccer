package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.payload.response.ReservationResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.repository.UserReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.service.UserReservationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserReservationServiceImpl implements UserReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepo;
    private final UserReservationRepository userReservationRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void add(Long reservation_id, Long user_id) {

        Reservation reservation = reservationRepository.findById(reservation_id).get();
        User user = userRepo.getOne(user_id);

        UserReservationEvent userReservationEvent = new UserReservationEvent();

        userReservationEvent.setReservation(reservation);
        userReservationEvent.setUser(user);
        userReservationEvent.setRegisteredAt(LocalDateTime.now());


        userReservationRepository.save(userReservationEvent);

    }

    @Override
    public List<BasicUserInfoResponse> findAllUsersByReservationID(Long reservation_id) {
        Reservation reservation = reservationRepository.findById(reservation_id).get();
        Set<UserReservationEvent> userReservationEvents = reservation.getUserReservationEvents();
        List<User> users = new ArrayList<>();


        for (UserReservationEvent e : userReservationEvents) {
            users.add(e.getUser());
        }
        List<BasicUserInfoResponse> responseList = new ArrayList<>();
        for (User u : users) {
            responseList.add(modelMapper.map(u, BasicUserInfoResponse.class));
        }

        return responseList;

    }
}

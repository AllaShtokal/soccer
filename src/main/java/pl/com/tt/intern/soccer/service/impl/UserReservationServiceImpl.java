package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.model.UserReservationEvent;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.repository.UserReservationRepository;
import pl.com.tt.intern.soccer.service.UserReservationService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    public void add(Long reservationId, Long userId) throws Exception {


        List<UserReservationEvent> allByUser_idAndReservation_id =
                userReservationRepository.findAllByUser_IdAndReservation_Id(userId, reservationId);
        if (!allByUser_idAndReservation_id.isEmpty()) {
          throw new Exception("User already is attached!");
        }
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new);
        User user = userRepo.getOne(userId);

        UserReservationEvent userReservationEvent = new UserReservationEvent();

        userReservationEvent.setReservation(reservation);
        userReservationEvent.setUser(user);
        userReservationEvent.setRegisteredAt(LocalDateTime.now());


        userReservationRepository.save(userReservationEvent);


    }

    @Override
    @Transactional
    public void remove(Long reservationId, Long userId) throws NotFoundException {

        List<UserReservationEvent> allEvents = userReservationRepository.findAll();
        for (UserReservationEvent e : allEvents) {
            if (e.getUser().getId().equals(userId) && e.getReservation().getId().equals(reservationId)) {

                reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new).removeUserReservationEvent(e);
                userRepo.findById(userId).orElseThrow(NotFoundException::new).removeUserReservationEvent(e);
                break;
            }
        }
    }

    @Override
    public List<BasicUserInfoResponse> findAllUsersByReservationID(Long reservationId) throws NotFoundException {
        reservationRepository.findAll();
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(NotFoundException::new);
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

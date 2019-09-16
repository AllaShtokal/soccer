package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.dto.ReservationPersistDTO;
import pl.com.tt.intern.soccer.dto.ReservationRetrieveDTO;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;
import pl.com.tt.intern.soccer.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;

    @Override
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) throws NotFoundException {
        return reservationRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    @Transactional
    public ReservationRetrieveDTO save(ReservationPersistDTO reservationPersistDTO) throws NotFoundException {
        ModelMapper mapper = new ModelMapper();
        Reservation reservation = mapper.map(reservationPersistDTO, Reservation.class);
        reservation.setConfirmed(false);
        reservation.setUser(userService.findById(reservationPersistDTO.getUserId()));
        Reservation savedEntity = reservationRepository.save(reservation);
        return mapper.map(savedEntity, ReservationRetrieveDTO.class);

    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public boolean isPersistedObjectOk(ReservationPersistDTO reservationPersistDTO) {
        // TODO: 16.09.2019 add verifications:
        // 1. is date in future?
        // 2. is is date-to after date-before
        // 3. is date 15-minute rounded
        return isDateRangeAvailable(reservationPersistDTO);
    }

    public boolean isDateRangeAvailable(ReservationPersistDTO reservationPersistDTO)  {
        LocalDateTime dateFrom = reservationPersistDTO.getDateFrom();
        LocalDateTime dateTo = reservationPersistDTO.getDateTo();
        return !reservationRepository.datesCollide(dateFrom, dateTo);
    }
}

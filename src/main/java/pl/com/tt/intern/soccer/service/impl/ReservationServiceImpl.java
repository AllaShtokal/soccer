package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.dto.ReservationPersistDTO;
import pl.com.tt.intern.soccer.dto.ReservationRetrieveDTO;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationException;
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
    private final ModelMapper mapper;

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
    public void verifyPersistedObject(ReservationPersistDTO reservationPersistDTO) throws ReservationException {
        isInFuture(reservationPersistDTO);
        isDateOrderOk(reservationPersistDTO);
        isDate15MinuteRounded(reservationPersistDTO.getDateFrom());
        isDate15MinuteRounded(reservationPersistDTO.getDateTo());
        isDateRangeAvailable(reservationPersistDTO);
    }

    public boolean isInFuture(ReservationPersistDTO reservationPersistDTO) throws ReservationException {
        boolean isInFuture = reservationPersistDTO.getDateFrom().isAfter(LocalDateTime.now());
        if (isInFuture) return true;
        else throw new ReservationException("Reservation exception: date should be in future");
    }

    public boolean isDateOrderOk(ReservationPersistDTO reservationPersistDTO) throws ReservationException {
        boolean isOrderOk = reservationPersistDTO.getDateFrom().isBefore(reservationPersistDTO.getDateTo());
        if (isOrderOk) return true;
        else throw new ReservationException("Reservation dates order is wrong. DateFrom should be before DateTo");
    }

    public boolean isDate15MinuteRounded(LocalDateTime time) throws ReservationException {
        if (time.getNano() != 0) throw new ReservationException("nanoseconds in reservations should be 0");
        if (time.getSecond() != 0) throw new ReservationException("seconds in reservations should be 0");
        if (time.getMinute()%15 !=0) throw new ReservationException("minutes in reservations should be rounded to 15");;
        return true;
    }

    public boolean isDateRangeAvailable(ReservationPersistDTO reservationPersistDTO) throws ReservationException {
        LocalDateTime dateFrom = reservationPersistDTO.getDateFrom();
        LocalDateTime dateTo = reservationPersistDTO.getDateTo();
        boolean isDateRangeAvailable = !reservationRepository.datesCollide(dateFrom, dateTo);
        if (isDateRangeAvailable) return true;
        else throw new ReservationException("Date range collision occured");
    }
}

package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.payload.request.ReservationPersistRequest;
import pl.com.tt.intern.soccer.payload.response.ReservationPersistedResponse;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.ReservationFormatException;
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
    public ReservationPersistedResponse save(ReservationPersistRequest reservationPersistDTO) throws NotFoundException {
        Reservation reservation = mapper.map(reservationPersistDTO, Reservation.class);
        reservation.setConfirmed(false);
        reservation.setUser(userService.findById(reservationPersistDTO.getUserId()));
        Reservation savedEntity = reservationRepository.save(reservation);
        return mapper.map(savedEntity, ReservationPersistedResponse.class);

    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    @Override
    public void verifyPersistedObject(ReservationPersistRequest reservationPersistDTO) throws ReservationFormatException {
        if (!isInFuture(reservationPersistDTO))
            throw new ReservationFormatException("Reservation exception: date must be in future");
        if (!isDateOrderOk(reservationPersistDTO))
            throw new ReservationFormatException("Reservation exception: wrong date order");
        if (!isDate15MinuteRounded(reservationPersistDTO.getDateFrom()))
            throw new ReservationFormatException("Reservation exception: date must be rounded to 15 minutes 0 s 0 ns");
        if (!isDate15MinuteRounded(reservationPersistDTO.getDateTo()))
            throw new ReservationFormatException("Reservation exception: date must be rounded to 15 minutes 0 s 0 ns");
        if (!isDateRangeAvailable(reservationPersistDTO))
            throw new ReservationFormatException("Reservation date range is already booked");
    }

    @Override
    public boolean isInFuture(ReservationPersistRequest reservationPersistDTO)  {
        return reservationPersistDTO.getDateFrom().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean isDateOrderOk(ReservationPersistRequest reservationPersistDTO)  {
        return reservationPersistDTO.getDateFrom().isBefore(reservationPersistDTO.getDateTo());
    }

    @Override
    public boolean isDate15MinuteRounded(LocalDateTime time) {
        if (time.getNano() != 0) return false;
        if (time.getSecond() != 0) return false;
        if (time.getMinute()%15 !=0) return false;
        return true;
    }

    @Override
    public boolean isDateRangeAvailable(ReservationPersistRequest reservationPersistDTO)  {
        LocalDateTime dateFrom = reservationPersistDTO.getDateFrom();
        LocalDateTime dateTo = reservationPersistDTO.getDateTo();
        return !reservationRepository.datesCollide(dateFrom, dateTo);

    }
}

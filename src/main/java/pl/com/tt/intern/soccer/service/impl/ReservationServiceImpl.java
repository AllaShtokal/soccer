package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.Reservation;
import pl.com.tt.intern.soccer.repository.ReservationRepository;
import pl.com.tt.intern.soccer.service.ReservationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

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
    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @Override
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
}

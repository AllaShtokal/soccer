package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;

public interface UserReservationService {
    void add(Long reservation_id, Long id) throws NotFoundException;
}

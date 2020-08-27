package pl.com.tt.intern.soccer.service;


import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;

import java.util.List;

public interface UserReservationService {
    void add(Long reservationId, Long userId) ;
    List<BasicUserInfoResponse> findAllUsersByReservationID (Long reservationId) throws NotFoundException;
    void remove(Long reservationId, Long userId) throws NotFoundException;
}

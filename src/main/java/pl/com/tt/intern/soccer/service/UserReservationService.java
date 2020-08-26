package pl.com.tt.intern.soccer.service;


import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;

import java.util.List;

public interface UserReservationService {
    void add(Long reservation_id, Long id) throws Exception;
    List<BasicUserInfoResponse> findAllUsersByReservationID (Long reservation_id) throws NotFoundException;

    void remove(Long reservation_id, Long id) throws NotFoundException;
}
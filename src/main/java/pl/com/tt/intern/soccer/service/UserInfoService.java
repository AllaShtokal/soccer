package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.UserInfo;

import java.util.List;

public interface UserInfoService {

    List<UserInfo> findAll();

    UserInfo findById(Long id) throws NotFoundException;

    UserInfo save(UserInfo userInfo);

    void deleteById(Long id);

}

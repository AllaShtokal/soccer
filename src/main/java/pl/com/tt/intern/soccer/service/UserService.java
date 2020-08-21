package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.response.UserRankingResponse;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Long id) throws NotFoundException;

    User save(User user);

    void deleteById(Long id);

    List<User> findByIdIn(List<Long> userIds);

    User findByEmail(String email) throws NotFoundException;

    User findByUsernameOrEmail(String username, String email) throws NotFoundException;

    User findByUsername(String username) throws NotFoundException;

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    void changeEnabledAccount(User user, Boolean enabled);

    void changePassword(User user, String password);

    User update(User user);

    void changeEmail(User user, String email);

    UserRankingResponse showRankingByUserId(Long userId);
}

package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.payload.response.UserRankingResponse;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        return userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public User save(User user) {
        try {
            String oldPassword = user.getPassword();
            String encodedPassword = encoder.encode(oldPassword);
            user.setPassword(encodedPassword);

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("User already exists");
        }
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) throws NotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public User findByUsernameOrEmail(String username, String email) throws NotFoundException {
        return userRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public List<User> findByIdIn(List<Long> userIds) {
        return userRepository.findByIdIn(userIds);
    }

    @Override
    public User findByUsername(String username) throws NotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void changeEnabledAccount(User user, Boolean enabled) {
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void changePassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public void changeEmail(User user, String email) {
        user.setEmail(email);
        userRepository.save(user);
    }

    @Override
    public UserRankingResponse showRankingByUserId(Long userId, int page, int size) throws NotFoundException {
        UserRankingResponse userRankingResponse = new UserRankingResponse();
        List<User> all1 = userRepository.findAll();

        User byId = userRepository.findById(userId).orElseThrow(NotFoundException::new);

        int index = all1.indexOf(byId);
        page = index / size ;


        BasicUserInfoResponse currentUser = new BasicUserInfoResponse();
        currentUser.setUsername(byId.getUsername());
        currentUser.setEmail(byId.getEmail());
        currentUser.setLost(byId.getUserInfo().getLost().toString());
        currentUser.setWon(byId.getUserInfo().getWon().toString());

        userRankingResponse.setCurrent(currentUser);

        userRankingResponse.setSize(size);
        userRankingResponse.setPage(page);

        Page<User> all = userRepository.findAll(PageRequest.of(page, size));
        List<User> content = all.getContent();
        List<BasicUserInfoResponse> users = new ArrayList<>();
        for (User u : content) {
            BasicUserInfoResponse userInfoResponse = new BasicUserInfoResponse();
            userInfoResponse.setUsername(u.getUsername());
            userInfoResponse.setEmail(u.getEmail());
            userInfoResponse.setWon(u.getUserInfo().getWon().toString());
            userInfoResponse.setLost(u.getUserInfo().getLost().toString());
            users.add(userInfoResponse);
        }

        userRankingResponse.setUsers(users);


        return userRankingResponse;

    }
}

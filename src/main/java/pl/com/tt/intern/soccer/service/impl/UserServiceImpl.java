package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final ModelMapper modelMapper;

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
    public UserRankingResponse showRankingByUserId(Long userId, String page, int size, String s1, String s2, String s3, String s4) throws NotFoundException {
        UserRankingResponse userRankingResponse = new UserRankingResponse();
        User byId = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        int index = userRepository.findAllAttachedToMeByUserId(userId);
        int pageI = 0;
        if (page == null) {
            pageI = index / size;
        }
        userRankingResponse.setUsername(byId.getUsername());
        userRankingResponse.setSize(size);
        userRankingResponse.setPage(pageI);

        List<BasicUserInfoResponse> users = getUsers(pageI, size, s1, s2, s3, s4);

        userRankingResponse.setUsers(users);
        userRankingResponse.setTotalSize(userRepository.getTotalNumber());
        return userRankingResponse;
    }


    public List<BasicUserInfoResponse> getUsers(int page, int size, String s1, String s2, String s3, String s4) {
        List<String[]> objects =
                userRepository.mySelect(PageRequest.of(page, size), s1, s2, s3, s4);
        List<BasicUserInfoResponse> users = new ArrayList<>();

        for (String[] o : objects) {

            BasicUserInfoResponse b = new BasicUserInfoResponse();
            b.setUsername(o[0]);
            b.setEmail(o[1]);
            b.setLost(o[2]);
            b.setWon(o[3]);
            b.setRanking(o[4]);
            users.add(b);
        }
        return users;

    }
}

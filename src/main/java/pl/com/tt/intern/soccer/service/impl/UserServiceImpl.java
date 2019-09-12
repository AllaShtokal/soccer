package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.service.UserService;

import javax.transaction.Transactional;
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

    @Override
    @Transactional
    public User save(User user) {
        try {
            String oldPassword = user.getPassword();
            String encodedPassword = encoder.encode(oldPassword);
            user.setPassword(encodedPassword);

            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("user already exists");
        }
    }

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
}

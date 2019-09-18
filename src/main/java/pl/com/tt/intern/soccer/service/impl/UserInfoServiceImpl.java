package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.UserInfo;
import pl.com.tt.intern.soccer.repository.UserInfoRepository;
import pl.com.tt.intern.soccer.service.UserInfoService;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final UserInfoRepository userInfoRepository;

    @Override
    public List<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }

    @Override
    public UserInfo findById(Long id) throws NotFoundException {
        return userInfoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public UserInfo save(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public UserInfo update(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        userInfoRepository.deleteById(id);
    }

}

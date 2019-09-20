package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKeyForSignUp;
import pl.com.tt.intern.soccer.repository.ConfirmationKeyForSignUpRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyForSignUpService;

@Service
@RequiredArgsConstructor
public class ConfirmationKeyForSignUpServiceImpl implements ConfirmationKeyForSignUpService {

    private final ConfirmationKeyForSignUpRepository confirmationKeyForSignUpRepository;

    @Override
    public ConfirmationKeyForSignUp save(ConfirmationKeyForSignUp confirmationKeyForSignUp) {
        return confirmationKeyForSignUpRepository.save(confirmationKeyForSignUp);
    }

    @Override
    public ConfirmationKeyForSignUp findConfirmationKeyByUuid(String uuid) throws NotFoundException {
        return confirmationKeyForSignUpRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Token not found in database."));
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void scanAndDeleteExpiredConfirmationKeys() {
        confirmationKeyForSignUpRepository.deleteByExpirationTime();
    }

}

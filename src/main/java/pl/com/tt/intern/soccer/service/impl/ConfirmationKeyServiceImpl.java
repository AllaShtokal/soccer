package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.repository.ConfirmationKeyRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;
import pl.com.tt.intern.soccer.service.UserService;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmationKeyServiceImpl implements ConfirmationKeyService {

    private final ConfirmationKeyRepository confirmationKeyRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ConfirmationKey save(ConfirmationKey confirmationKey) {
        return confirmationKeyRepository.save(confirmationKey);
    }

    @Override
    public ConfirmationKey findConfirmationKeyByUuid(String uuid) throws NotFoundException {
        return confirmationKeyRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Token not found in database."));
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void scanAndDeleteExpiredConfirmationKeys() {
        confirmationKeyRepository.deleteByExpirationTime();
    }

    @Override
    public ConfirmationKey createAndAssignToUserByEmail(String email) throws NotFoundException {
        return confirmationKeyRepository.save(
                new ConfirmationKey(
                        userService.findByEmail(email)
                ));
    }
}

package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;
import pl.com.tt.intern.soccer.repository.ConfirmationKeyRepository;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;

@Service
@RequiredArgsConstructor
public class ConfirmationKeyServiceImpl implements ConfirmationKeyService {

    private final ConfirmationKeyRepository confirmationKeyRepository;

    @Override
    public ConfirmationKey save(ConfirmationKey confirmationKey) {
        return confirmationKeyRepository.save(confirmationKey);
    }

    @Override
    public ConfirmationKey findConfirmationKeyByUuid(String uuid) throws NotFoundException {
        return confirmationKeyRepository.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Token not found in database."));
    }
}

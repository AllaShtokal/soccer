package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.model.ConfirmationKey;

public interface ConfirmationKeyService {

    ConfirmationKey save(ConfirmationKey token);

    ConfirmationKey findConfirmationKeyByUuid(String uuid) throws NotFoundException;

}

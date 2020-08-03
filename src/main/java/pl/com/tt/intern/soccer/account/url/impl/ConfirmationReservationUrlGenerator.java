package pl.com.tt.intern.soccer.account.url.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;
import pl.com.tt.intern.soccer.account.url.AbstractAccountChangeUrlGenerator;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConfirmationReservationUrlGenerator extends AbstractAccountChangeUrlGenerator {

    @Getter
    private final String urlSuffix = "reservations";

    @Override
    public boolean supports(AccountChangeType type) {
        return CONFIRM_RESERVATION.equals(type);
    }
}

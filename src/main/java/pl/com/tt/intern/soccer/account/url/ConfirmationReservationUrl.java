package pl.com.tt.intern.soccer.account.url;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.account.factory.AccountChangeType;

import static pl.com.tt.intern.soccer.account.factory.AccountChangeType.CONFIRM_RESERVATION;

@Slf4j
@Service
public class ConfirmationReservationUrl implements ChangeAccountUrlGenerator {

    @Value("${properties.account.confirmation-reservation.mail.link}")
    private String link;

    @Value("${frontend.server.address}")
    private String serverAddress;

    @Value("${frontend.server.port}")
    private String serverPort;


    @Override
    public String generate(String email, String... params) {
        return serverAddress + ":" + serverPort + link + params[0];
    }

    @Override
    public boolean supports(AccountChangeType type) {
        return CONFIRM_RESERVATION.equals(type);
    }
}

package pl.com.tt.intern.soccer.account.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AccountChangeType {
    NOT_LOGGED_IN_USER_PASSWORD(201),
    ACTIVE_ACCOUNT(202),
    EMAIL(203),
    CONFIRM_RESERVATION(204);

    private final int value;

    public static AccountChangeType valueOf(int type) {
        return Arrays.stream(values()).
                filter(v -> v.value == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Type not exist.."));
    }
}

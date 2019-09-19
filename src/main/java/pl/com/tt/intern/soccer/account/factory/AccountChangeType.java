package pl.com.tt.intern.soccer.account.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum AccountChangeType {
    NOT_LOGGED_IN_USER_PASSWORD(201),
    ACTIVE_ACCOUNT(202);

    private final int value;
    private static Map map = new HashMap<>();

    public static AccountChangeType valueOf(int type) {
        return Arrays.stream(values()).
                filter(v -> v.value == type)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Type not exist.."));
    }
}

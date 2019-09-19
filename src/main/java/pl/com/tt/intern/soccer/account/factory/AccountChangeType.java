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

    private int value;
    private static Map map = new HashMap<>();

    AccountChangeType(int value) {
        this.value = value;
    }

    static {
        Arrays.stream(AccountChangeType.values())
                .forEach(type -> map.put(type.value, type));
    }

    public static AccountChangeType valueOf(int accountChangeType) {
        return (AccountChangeType) map.get(accountChangeType);
    }
}

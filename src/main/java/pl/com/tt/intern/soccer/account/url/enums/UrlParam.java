package pl.com.tt.intern.soccer.account.url.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UrlParam {

    NEW_EMAIL("newEmail"),
    ACTIVE_KEY("activeKey"),
    CHANGE_EMAIL_KEY("changeEmailKey"),
    CHANGE_PASSWORD_KEY("changePasswordKey");

    private final String parameterName;
}

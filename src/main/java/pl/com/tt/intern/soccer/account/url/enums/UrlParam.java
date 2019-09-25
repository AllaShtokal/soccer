package pl.com.tt.intern.soccer.account.url.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UrlParam {

    NEW_EMAIL("newEmail"),
    ACTIVE_KEY("activeKey"),
    CHANGE_EMAIL_KEY("changeEmailKey"),
    CHANGE_PASSWORD_KEY("changePasswordKey");

    private String parameterName;
}

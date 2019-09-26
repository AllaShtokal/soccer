package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

@Data
public class PasswordChangeKeyResponse {
    private final String passwordChangeKey;
}

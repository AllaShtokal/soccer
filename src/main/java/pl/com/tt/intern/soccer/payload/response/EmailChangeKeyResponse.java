package pl.com.tt.intern.soccer.payload.response;

import lombok.Data;

@Data
public class EmailChangeKeyResponse {
    private final String emailChangeKey;
    private final String newEmail;
}

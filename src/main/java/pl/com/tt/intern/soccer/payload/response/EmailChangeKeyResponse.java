package pl.com.tt.intern.soccer.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailChangeKeyResponse {
    private String emailChangeKey;
    private String newEmail;
}

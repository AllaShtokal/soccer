package pl.com.tt.intern.soccer.payload.request;

import lombok.Data;

import java.util.Set;

@Data
public class TeamRequest {

    String teamName;
    Set<String> usernames;
}

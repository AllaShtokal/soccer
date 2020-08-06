package pl.com.tt.intern.soccer.payload.response;


import lombok.Data;

@Data
public class ButtleResponse {

    private Long id;
    private String teamName1;
    private String teamName2;
    private int scoreTeam1;
    private int scoreTeam2;



}

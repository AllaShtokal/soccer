package pl.com.tt.intern.soccer.payload.response;


import lombok.Data;

import java.util.List;

@Data
public class UserRankingResponse {

    private BasicUserInfoResponse current;
    private List<BasicUserInfoResponse> users;


}

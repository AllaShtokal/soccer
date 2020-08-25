package pl.com.tt.intern.soccer.payload.response;


import lombok.Data;

import java.util.List;

@Data
public class UserRankingResponse {

    private String Username;
    private List<BasicUserInfoResponse> users;
    private int page;
    private int size;
    private Long totalSize;


}

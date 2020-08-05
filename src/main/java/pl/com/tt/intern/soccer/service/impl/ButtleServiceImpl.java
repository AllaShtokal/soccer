package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Buttle;
import pl.com.tt.intern.soccer.model.Team;

@Service
@RequiredArgsConstructor
public class ButtleServiceImpl {


    //if first and second is the same?????!
    String getTeamWinner(Buttle buttle){
        if(buttle.getScoreTeam1()>buttle.getScoreTeam2())
            return buttle.getTeamName1();
            //if first and second is the same?????! for now it also returns second team
        else return buttle.getTeamName2();
    }


}

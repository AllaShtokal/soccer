package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Team;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.repository.TeamRepository;
import pl.com.tt.intern.soccer.repository.UserRepository;
import pl.com.tt.intern.soccer.service.TeamService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public Long getTeamIdByTeamNameAndMatchId(String name, Long matchId) {
        return teamRepository.findByNameAndMatchm_Id(name, matchId).getId();
    }

    @Override
    public void save(Team team) {

        teamRepository.save(team);

    }


    @Override
    public Set<BasicUserInfoResponse> getUsersByTeamNameAndMatchId(String teamName, Long matchId) {
        Set<User> users = teamRepository.findByNameAndMatchm_Id(teamName, matchId).getUsers();
        Set<BasicUserInfoResponse> usersResponse = new HashSet<>();
        for (User u : users) {
            BasicUserInfoResponse bu = modelMapper.map(u, BasicUserInfoResponse.class);
            bu.setWon(u.getUserInfo().getWon().toString());
            bu.setLost(u.getUserInfo().getLost().toString());
            bu.setRanking(userRepository.getRankByUsername( u.getUsername() ).toString());
            usersResponse.add(bu);
        }
        return usersResponse;
    }

}

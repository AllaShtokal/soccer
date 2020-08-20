package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.User;
import pl.com.tt.intern.soccer.payload.response.BasicUserInfoResponse;
import pl.com.tt.intern.soccer.repository.TeamRepository;
import pl.com.tt.intern.soccer.service.TeamService;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final ModelMapper modelMapper;

    @Override
    public Long getTeamIdByTeamName(String teamName) {
        return teamRepository.findByName(teamName).getId();
    }

    @Override
    public Set<BasicUserInfoResponse> getUsersByTeamName(String teamName) {
        Set<User> users = teamRepository.findByName(teamName).getUsers();
        Set<BasicUserInfoResponse> usersResponse = new HashSet<>();
        for (User u : users) {

            BasicUserInfoResponse bu = modelMapper.map(u, BasicUserInfoResponse.class);
            usersResponse.add(bu);
        }
        return usersResponse;
    }

}

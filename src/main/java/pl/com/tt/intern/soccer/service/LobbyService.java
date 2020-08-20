package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.payload.request.LobbyChangeRequest;
import pl.com.tt.intern.soccer.payload.request.LobbyRequest;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;

import java.util.List;

public interface LobbyService {


    LobbyResponse findByName(String name);

    List<LobbyResponse> findAllAvailable();

    LobbyRequest save(LobbyRequest lobbyRequest);

    LobbyChangeRequest update(LobbyChangeRequest lobbyRequest, String name);
}

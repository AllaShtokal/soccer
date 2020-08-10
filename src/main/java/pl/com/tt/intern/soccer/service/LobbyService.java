package pl.com.tt.intern.soccer.service;

import pl.com.tt.intern.soccer.model.Lobby;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;

import java.util.List;

public interface LobbyService {


    Lobby getByName(String name);

    List<LobbyResponse>  findAll();
}

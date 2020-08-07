package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.model.Lobby;
import pl.com.tt.intern.soccer.repository.LobbyRepository;
import pl.com.tt.intern.soccer.service.LobbyService;

@Service
@RequiredArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private LobbyRepository lobbyRepository;

    @Override
    public Lobby getByName(String name) {
        return lobbyRepository.findFirstByName(name);
    }
}

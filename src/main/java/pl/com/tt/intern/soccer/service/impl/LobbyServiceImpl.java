package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.tt.intern.soccer.exception.NotFoundLobbyByIdException;
import pl.com.tt.intern.soccer.model.Lobby;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;
import pl.com.tt.intern.soccer.repository.LobbyRepository;
import pl.com.tt.intern.soccer.service.LobbyService;

import javax.persistence.Lob;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    @Override

    public Lobby getByName(String name) {
        return lobbyRepository.findFirstByName(name).orElseThrow(() -> new NotFoundLobbyByIdException(name));
    }

    @Override
    public List<LobbyResponse> findAllAvailable() {
        //dostać tylko dostępne
        List<Lobby> lobbies = lobbyRepository.findAll();
        List<LobbyResponse> lobbyResponses = new ArrayList<>();
        for(Lobby l: lobbies)
        {
            if(l.getAvailable())
                lobbyResponses.add(new LobbyResponse(l.getName()));

        }
        return lobbyResponses;
    }

}

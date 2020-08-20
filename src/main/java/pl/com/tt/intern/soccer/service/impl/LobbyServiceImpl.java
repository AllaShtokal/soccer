package pl.com.tt.intern.soccer.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.com.tt.intern.soccer.exception.NotFoundLobbyByIdException;
import pl.com.tt.intern.soccer.model.Lobby;
import pl.com.tt.intern.soccer.payload.request.LobbyChangeRequest;
import pl.com.tt.intern.soccer.payload.request.LobbyRequest;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;
import pl.com.tt.intern.soccer.repository.LobbyRepository;
import pl.com.tt.intern.soccer.service.LobbyService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final ModelMapper modelMapper;

    @Override
    public LobbyResponse findByName(String name) {
        Lobby lobby = lobbyRepository.findFirstByName(name).orElseThrow(() -> new NotFoundLobbyByIdException(name));
        return modelMapper.map(lobby,LobbyResponse.class );
    }

    @Override
    public List<LobbyResponse> findAllAvailable() {
        List<Lobby> lobbies = lobbyRepository.findAll();
        List<LobbyResponse> lobbyResponses = new ArrayList<>();
        for (Lobby l : lobbies) {
            if (l.getAvailable())
                lobbyResponses.add(new LobbyResponse(l.getName()));

        }
        return lobbyResponses;
    }

    @Override
    @Transactional
    public LobbyRequest save(LobbyRequest lobbyRequest) {
        Lobby lobby = modelMapper.map(lobbyRequest, Lobby.class);
        Lobby savedEntity = lobbyRepository.save(lobby);
        return modelMapper.map(savedEntity, LobbyRequest.class);
    }


    @Override
    @Transactional
    public LobbyChangeRequest update(LobbyChangeRequest lobbyRequest, String name) {
        Lobby lobby = lobbyRepository.findFirstByName(name)
                .orElseThrow(() -> new NotFoundLobbyByIdException(name));
        if (lobbyRequest.getAvailable() != null) lobby.setAvailable(lobbyRequest.getAvailable());
        if (lobbyRequest.getLimit() != null) lobby.setLimit(lobbyRequest.getLimit());
        if (lobbyRequest.getName() != null) lobby.setName(lobbyRequest.getName());
        lobbyRepository.save(lobby);

        return modelMapper.map(lobby, LobbyChangeRequest.class);


    }


}

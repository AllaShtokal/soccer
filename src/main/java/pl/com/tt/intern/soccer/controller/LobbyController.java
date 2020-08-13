package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;
import pl.com.tt.intern.soccer.service.LobbyService;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lobby")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class LobbyController {
    private final LobbyService lobbyService;


    @GetMapping
    public ResponseEntity<List<LobbyResponse>> findAll() {
        return ok(lobbyService.findAll());
    }



}

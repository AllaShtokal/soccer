package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.payload.request.LobbyChangeRequest;
import pl.com.tt.intern.soccer.payload.request.LobbyRequest;
import pl.com.tt.intern.soccer.payload.response.LobbyResponse;
import pl.com.tt.intern.soccer.service.LobbyService;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
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
        return ok(lobbyService.findAllAvailable());
    }

    @GetMapping("/{name}")
    public ResponseEntity<LobbyResponse> findByName(@PathVariable String name) {
        return ok(lobbyService.findByName(name));
    }

    @PostMapping
    public ResponseEntity<LobbyRequest> create(@Valid @RequestBody LobbyRequest lobbyRequest) {

        return ResponseEntity
                .status(CREATED)
                .body(lobbyService.save(lobbyRequest));
    }

    @PostMapping("/{name}")
    public ResponseEntity<LobbyChangeRequest> update(@Valid @RequestBody LobbyChangeRequest lobbyRequest,
                                                     @PathVariable String name) {

        return ResponseEntity
                .status(CREATED)
                .body(lobbyService.update(lobbyRequest, name));
    }





}

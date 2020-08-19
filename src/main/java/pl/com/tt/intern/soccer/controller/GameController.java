package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.service.GameService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class GameController {

    private final GameService gameService;


}

package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.response.UserRankingResponse;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.UserService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@PreAuthorize("isAuthenticated()")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<UserRankingResponse> showRanking(@CurrentUser UserPrincipal user,
                                                           @RequestParam(value = "size", required = false, defaultValue = "3") Integer size,
                                                           @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                           @RequestParam(value = "page", required = false, defaultValue = "ui.won") String field1,
                                                           @RequestParam(value = "page", required = false, defaultValue = "DESC") String order1,
                                                           @RequestParam(value = "page", required = false, defaultValue = "ui.lost") String field2,
                                                           @RequestParam(value = "page", required = false, defaultValue = "ASC") String order2
                                                           ) throws NotFoundException {
        UserRankingResponse userRankingResponse = userService.showRankingByUserId(user.getId(),
                page,
                size,
                field1,
                order1,
                field2,
                order2);
        return ok(userRankingResponse);
    }


}

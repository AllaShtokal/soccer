package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.exception.IncorrectTokenException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.service.AccountService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PatchMapping
    public ResponseEntity<?> activateAccount(@RequestParam(name = "activeToken") String activeToken) throws IncorrectTokenException {
        accountService.activateAccountByToken(activeToken);
        return ok().build();
    }

    @GetMapping("/change/password")
    public ResponseEntity<?> sendMailToChangePassword(@RequestParam(name = "email") String email) throws NotFoundException {
        accountService.sendMailToChangePassword(email);
        return ok().build();
    }

    @PatchMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestParam(name = "changePasswordToken") String changePasswordToken,
                                            @Valid @RequestBody ChangePasswordRequest request) throws Exception {
        accountService.changePassword(changePasswordToken, request);
        return ok().build();
    }

}

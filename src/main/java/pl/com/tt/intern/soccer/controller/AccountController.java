package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> activateAccount(@RequestParam(name = "activeConfirmKey") String activeConfirmKey) throws IncorrectTokenException {
        accountService.activateAccountByToken(activeConfirmKey);
        return ok().build();
    }

    @GetMapping("/change/password")
    public ResponseEntity<?> sendMailToChangePassword(@RequestParam(name = "email") String email) throws NotFoundException {
        accountService.sendMailToChangePassword(email);
        return ok().build();
    }

    @PatchMapping("/change/password")
    public ResponseEntity<?> changePassword(@RequestParam(name = "changePasswordConfirmKey") String changePasswordConfirmKey,
                                            @Valid @RequestBody ChangePasswordRequest request) throws Exception {
        accountService.changePassword(changePasswordConfirmKey, request);
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/deactivate/{id}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Long id) throws NotFoundException {
        accountService.deactivate(id);
        return ok().build();
    }
}

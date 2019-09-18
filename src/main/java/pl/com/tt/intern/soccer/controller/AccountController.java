package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.security.UserPrincipal;
import pl.com.tt.intern.soccer.service.AccountService;

import javax.validation.Valid;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PatchMapping(params = "activationKey")
    public ResponseEntity<?> activateAccount(@RequestParam(name = "activationKey") String activationKey) throws IncorrectConfirmationKeyException {
        accountService.activateAccountByConfirmationKey(activationKey);
        return ok().build();
    }

    @GetMapping(value = "/change", params = "email")
    public ResponseEntity<?> sendMailToChangePassword(@RequestParam(name = "email") String email) throws NotFoundException {
        accountService.sendMailToChangePassword(email);
        return ok().build();
    }

    @PatchMapping(value = "/change", params = "changePasswordKey")
    public ResponseEntity<?> changePasswordNotLoggedUser(@RequestParam(name = "changePasswordKey") String changePasswordKey,
                                                         @Valid @RequestBody ForgottenPasswordRequest request) throws Exception {
        accountService.changePasswordNotLoggedInUser(changePasswordKey, request);
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/deactivate")
    public ResponseEntity<?> deactivateAccount(@CurrentUser UserPrincipal user) throws NotFoundException {
        accountService.deactivate(user.getId());
        return ok().build();
    }
}

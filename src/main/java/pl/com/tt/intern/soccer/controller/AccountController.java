package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pl.com.tt.intern.soccer.annotation.CurrentUser;
import pl.com.tt.intern.soccer.exception.IncorrectConfirmationKeyException;
import pl.com.tt.intern.soccer.exception.InvalidChangePasswordException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.exception.PasswordsMismatchException;
import pl.com.tt.intern.soccer.payload.request.ChangeAccountDataRequest;
import pl.com.tt.intern.soccer.payload.request.ChangePasswordRequest;
import pl.com.tt.intern.soccer.payload.request.EmailRequest;
import pl.com.tt.intern.soccer.payload.request.ForgottenPasswordRequest;
import pl.com.tt.intern.soccer.payload.response.ChangeDataAccountResponse;
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
    public ResponseEntity<String> activateAccount(@RequestParam(name = "activationKey") String activationKey)
            throws IncorrectConfirmationKeyException {
        accountService.activateAccountByConfirmationKey(activationKey);
        return ok().build();
    }

    @GetMapping(value = "/change", params = "email")
    public ResponseEntity<String> sendMailToChangePassword(@RequestParam(name = "email") String email) {
        accountService.setAndSendMailToChangePassword(email);
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/change/email", params = {"email", "newEmail"})
    public ResponseEntity<?> sendMailToChangeEmail(@RequestParam String email, @RequestParam String newEmail) {
        accountService.setAndSendMailToChangeEmail(email, newEmail);
        return ok().build();
    }

    @PatchMapping(value = "/change", params = "changePasswordKey")
    public ResponseEntity<String> changePasswordNotLoggedInUser(@RequestParam(name = "changePasswordKey") String changePasswordKey,
                                                                @Valid @RequestBody ForgottenPasswordRequest request)
            throws PasswordsMismatchException, IncorrectConfirmationKeyException {
        accountService.changePasswordNotLoggedInUser(changePasswordKey, request);
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/deactivate")
    public ResponseEntity<String> deactivateAccount(@CurrentUser UserPrincipal user) throws NotFoundException {
        accountService.deactivate(user.getId());
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/change/password")
    public ResponseEntity<String> changePasswordLoggedInUser(@CurrentUser UserPrincipal user,
                                                             @Valid @RequestBody ChangePasswordRequest request) throws InvalidChangePasswordException {
        accountService.changePasswordLoggedInUser(user, request);
        return ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/change")
    public ResponseEntity<ChangeDataAccountResponse> changeBasicAccountData(@CurrentUser UserPrincipal user,
                                                                            @Valid @RequestBody ChangeAccountDataRequest request) throws NotFoundException {
        return ok(accountService.changeUserInfo(user, request));
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping(value = "/change/email", params = "changeEmailKey")
    public ResponseEntity<?> changeEmail(@CurrentUser UserPrincipal user,
                                         @RequestParam(name = "changeEmailKey") String changeEmailKey,
                                         @Valid @RequestBody EmailRequest request) throws Exception {
        accountService.changeEmail(user, changeEmailKey, request);
        return ok().build();
    }
}

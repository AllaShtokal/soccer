package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.exception.ActivationAccountException;
import pl.com.tt.intern.soccer.exception.NotFoundException;
import pl.com.tt.intern.soccer.service.AccountService;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PatchMapping
    public ResponseEntity<?> activateAccount(@RequestParam(name = "activeToken") String activeToken) throws ActivationAccountException {
        accountService.activateAccountByToken(activeToken);
        return ok().build();
    }

    @PatchMapping("/change/password")
    public ResponseEntity<?> sendMailToChangePassword(@RequestParam(name = "email") String email) throws NotFoundException {
        accountService.sendMailToChangePassword(email);
        return ok().build();
    }

}

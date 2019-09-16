package pl.com.tt.intern.soccer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.tt.intern.soccer.service.ConfirmationKeyService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/confirmationKey")
public class ConfirmationKeyController {

    private final ConfirmationKeyService confirmationKeyService;

    @GetMapping("scan")
    public ResponseEntity<String> cleanExpiredConfirmationKeys(){
        confirmationKeyService.scanAndDeleteExpiredConfirmationKeys();
        return ResponseEntity.ok("Start scanning");
    }
}

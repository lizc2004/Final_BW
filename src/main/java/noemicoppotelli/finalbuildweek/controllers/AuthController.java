package noemicoppotelli.finalbuildweek.controllers;

import lombok.RequiredArgsConstructor;
import noemicoppotelli.finalbuildweek.payloads.AuthResponseDTO;
import noemicoppotelli.finalbuildweek.payloads.LoginRequestDTO;
import noemicoppotelli.finalbuildweek.payloads.RegisterRequestDTO;
import noemicoppotelli.finalbuildweek.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequestDTO request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
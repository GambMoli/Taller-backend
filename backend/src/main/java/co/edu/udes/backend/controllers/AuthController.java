package co.edu.udes.backend.controllers;

import co.edu.udes.backend.dto.login.JwtResponseDTO;
import co.edu.udes.backend.dto.login.LoginRequestDTO;
import co.edu.udes.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        Optional<JwtResponseDTO> response = authService.login(loginRequest);

        if (response.isPresent()) {
            return ResponseEntity.ok(response.get());
        } else {
            return ResponseEntity.badRequest().body("Credenciales inválidas");
        }
    }
}
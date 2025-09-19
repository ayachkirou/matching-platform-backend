package net.java.guides.matching.controller;

import net.java.guides.matching.dto.LoginRequestDTO;
import net.java.guides.matching.dto.LoginResponseDTO;
import net.java.guides.matching.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        try {
            LoginResponseDTO response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur interne du serveur");
        }
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                String jwt = token.substring(7);
                boolean isValid = authService.validateToken(jwt);
                return ResponseEntity.ok().body("{\"valid\":" + isValid + "}");
            }
            return ResponseEntity.ok().body("{\"valid\":false}");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la validation du token");
        }
    }
}
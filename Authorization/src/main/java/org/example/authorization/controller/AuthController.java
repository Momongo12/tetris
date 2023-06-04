package org.example.authorization.controller;

import org.example.authorization.model.dto.LoginRequestDto;
import org.example.authorization.model.dto.RegistrationRequestDto;
import org.example.authorization.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for handling authentication requests.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructs a new AuthController with the given AuthService.
     *
     * @param authService the authentication service
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param loginRequest the DTO containing the username and password
     * @return a response entity with a message indicating success or failure
     */
    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginRequestDto loginRequest) {
        boolean isAuthenticated = authService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (isAuthenticated) {
            return ResponseEntity.ok("Authentication successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }

    /**
     * Registers a new player with the provided information.
     *
     * @param registrationRequest the DTO containing the player's registration information
     * @return a response entity with a message indicating success or failure
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerPlayer(@RequestBody RegistrationRequestDto registrationRequest) {
        boolean isRegistered = authService.registerPlayer(registrationRequest.getName(),
                registrationRequest.getUsername(), registrationRequest.getEmail(), registrationRequest.getPassword());

        if (isRegistered) {
            return ResponseEntity.ok("Registration successful");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed");
        }
    }
}
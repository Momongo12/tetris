package tetris.service;

import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;

/**
 * The AuthService interface provides methods for authentication and player registration.
 * @author denMoskvin
 * @version 1.0
 */
public interface AuthService {
    /**
     * Authenticates a user with the provided login credentials.
     *
     * @param loginRequestDto the DTO containing the login credentials
     * @return true if the authentication is successful, false otherwise
     */
    boolean authenticate(LoginRequestDto loginRequestDto);

    /**
     * Registers a new player with the provided registration information.
     *
     * @param registrationRequestDto the DTO containing the player's registration information
     * @return true if the registration is successful, false otherwise
     */
    boolean registerPlayer(RegistrationRequestDto registrationRequestDto);
}
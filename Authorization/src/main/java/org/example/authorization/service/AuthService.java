package org.example.authorization.service;

/**
 * The AuthService interface provides methods for authentication and player registration.
 */
public interface AuthService {

    /**
     * Authenticates a user with the provided credentials.
     *
     * @param email the username
     * @param password the password
     * @return true if the user is authenticated, false otherwise
     */
    boolean authenticate(String email, String password);

    /**
     * Registers a new player with the provided information.
     *
     * @param name     the player's name
     * @param username the player's username
     * @param email    the player's email
     * @param password the player's password
     * @return true if the player is successfully registered, false otherwise
     */
    boolean registerPlayer(String name, String username, String email, String password);
}
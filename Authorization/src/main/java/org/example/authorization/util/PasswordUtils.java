package org.example.authorization.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class for encrypting passwords using BCryptPasswordEncoder.
 */
public class PasswordUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Encrypts a password using BCryptPasswordEncoder.
     *
     * @param password the original password to encrypt
     * @return the encrypted password
     */
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Checks if a password matches a hashed password.
     *
     * @param password        the password to check
     * @param hashedPassword  the hashed password to compare against
     * @return true if the password matches the hashed password, false otherwise
     */
    public static boolean checkPassword(String password, String hashedPassword) {
        return passwordEncoder.matches(password, hashedPassword);
    }
}
package org.example.authorization.service.impl;

import org.example.authorization.model.entity.Player;
import org.example.authorization.repository.PlayerRepository;
import org.example.authorization.service.AuthService;
import org.example.authorization.util.PasswordUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * The AuthServiceImpl class implements the {@link  AuthService} interface.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final PlayerRepository playerRepository;

    public AuthServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean authenticate(String email, String password) {
        Optional<Player> playerOptional = playerRepository.getPlayerByEmail(email);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            if (PasswordUtils.checkPassword(password, player.getPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean registerPlayer(String name, String username, String email, String password) {
        Optional<Player> playerOptional = playerRepository.getPlayerByEmail(email);

        if (playerOptional.isPresent()) {
            return false;
        }else {
            System.out.println(name + " " + username + " " + email + " " + password);
            System.out.println(PasswordUtils.encryptPassword(password));
            Player player = new Player(name.trim(), username.trim(), email.trim(), PasswordUtils.encryptPassword(password.trim()));
            playerRepository.save(player);
            return true;
        }
    }
}
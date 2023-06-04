package org.example.authorization.repository;

import org.example.authorization.model.entity.Player;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    Optional<Player> getPlayerByEmail(String email);
}

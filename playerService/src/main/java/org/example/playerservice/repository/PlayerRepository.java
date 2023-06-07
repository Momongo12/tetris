package org.example.playerservice.repository;


import org.example.playerservice.model.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {

    Optional<Player> findByEmail(String email);
}

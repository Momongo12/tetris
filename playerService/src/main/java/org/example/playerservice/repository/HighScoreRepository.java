package org.example.playerservice.repository;

import org.example.playerservice.model.entity.HighScore;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HighScoreRepository extends CrudRepository<HighScore, Long> {
    @Query("SELECT * FROM high_scores LEFT JOIN players ON high_scores.player_id = players.player_id")
    List<HighScore> findAllWithPlayer();
}

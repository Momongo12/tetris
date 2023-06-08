package org.example.playerservice.service;

import org.example.playerservice.model.dto.ScoreEntryDto;
import org.example.playerservice.model.entity.Player;

import java.util.List;

public interface HighScoresService {

    void updateHighScores(Player player);

    List<ScoreEntryDto> getHighScores();
}

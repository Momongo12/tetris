package org.example.playerservice.service.impl;

import org.example.playerservice.model.dto.ScoreEntryDto;
import org.example.playerservice.model.entity.HighScore;
import org.example.playerservice.model.entity.Player;
import org.example.playerservice.model.entity.PlayerStatistic;
import org.example.playerservice.repository.HighScoreRepository;
import org.example.playerservice.repository.PlayerRepository;
import org.example.playerservice.service.HighScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HighScoresServiceImpl implements HighScoresService {

    @Autowired
    private final HighScoreRepository highScoreRepository;

    @Autowired
    private final PlayerRepository playerRepository;

    public HighScoresServiceImpl(HighScoreRepository highScoreRepository, PlayerRepository playerRepository) {
        this.highScoreRepository = highScoreRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public void updateHighScores(Player player) {
        Optional<HighScore> highScoreOptional = highScoreRepository.getHighScoreByPlayerId(player.getPlayerId());
        PlayerStatistic playerStatistic = player.getPlayerStatistic();
        HighScore highScore;

        if (highScoreOptional.isPresent()) {
            highScore = highScoreOptional.get();

            if (playerStatistic.getMaxScore() > highScore.getMaxScore()) {
                highScore.setMaxScore(playerStatistic.getMaxScore());
            }else if (playerStatistic.getMaxLevel() > highScore.getMaxLevel()) {
                highScore.setMaxLevel(playerStatistic.getMaxLevel());
            }else if (playerStatistic.getMaxLines() > highScore.getMaxLines()) {
                highScore.setMaxLines(playerStatistic.getMaxLines());
            }
        }else {
            highScore = new HighScore(playerStatistic.getMaxScore(), playerStatistic.getMaxLevel(),
                    playerStatistic.getMaxLines(), player.getPlayerId());
        }

        highScoreRepository.save(highScore);
    }

    @Override
    public List<ScoreEntryDto> getHighScores() {
        Iterable<HighScore> highScores = highScoreRepository.findAll();
        List<ScoreEntryDto> scoreEntryDtos = new ArrayList<>();

        for (HighScore highScore: highScores) {
            String username = playerRepository.findById(highScore.getPlayerId()).get().getUsername();
            ScoreEntryDto scoreEntryDto = new ScoreEntryDto(username, highScore.getMaxScore(), highScore.getMaxLevel(),
                    highScore.getMaxLines());

            scoreEntryDtos.add(scoreEntryDto);
        }

        return scoreEntryDtos;
    }
}

package org.example.playerservice.service.impl;

import org.example.playerservice.exception.PlayerNotFoundException;
import org.example.playerservice.model.dto.PlayerDto;
import org.example.playerservice.model.entity.Player;
import org.example.playerservice.model.entity.PlayerStatistic;
import org.example.playerservice.repository.PlayerRepository;
import org.example.playerservice.repository.PlayerStatisticRepository;
import org.example.playerservice.service.HighScoresService;
import org.example.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private final PlayerRepository playerRepository;

    @Autowired
    private final PlayerStatisticRepository playerStatisticRepository;

    @Autowired
    private final HighScoresService highScoresService;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerStatisticRepository playerStatisticRepository, HighScoresService highScoresService) {
        this.playerRepository = playerRepository;
        this.playerStatisticRepository = playerStatisticRepository;
        this.highScoresService = highScoresService;
    }

    public PlayerDto getPlayerDtoByEmailPlayer(String email) throws PlayerNotFoundException {
        Optional<Player> playerOptional = playerRepository.findByEmail(email);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            PlayerStatistic playerStat = player.getPlayerStatistic();
            if (playerStat == null) {
                playerStat = new PlayerStatistic(player.getPlayerId());
                playerStatisticRepository.save(playerStat);
            }
            return new PlayerDto(player.getPlayerId(), player.getUsername(), player.getDateOfRegistration(), playerStat.getNumberOfGames(),
                    playerStat.getMaxScore(), playerStat.getMaxLines(), playerStat.getMaxLevel(), playerStat.getAverageScore());
        }else {
            throw new PlayerNotFoundException();
        }
    }

    public void updateStatisticPlayerByPlayerId(Long playerId, PlayerDto playerDto) throws PlayerNotFoundException{
        Optional<Player> playerOptional = playerRepository.findById(playerId);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            PlayerStatistic playerStat = player.getPlayerStatistic();

            if (playerStat == null) {
                playerStat = new PlayerStatistic(player.getPlayerId());
            }

            playerStat.setAverageScore(playerDto.getAverageScore());
            playerStat.setMaxLevel(playerDto.getMaxLevel());
            playerStat.setMaxLines(playerDto.getMaxLines());
            playerStat.setMaxScore(playerDto.getMaxScore());
            playerStat.setNumberOfGames(playerDto.getNumberOfGames());

            player.setPlayerStatistic(playerStat);
            playerStatisticRepository.save(playerStat);

            highScoresService.updateHighScores(player);
        }else {
            throw new PlayerNotFoundException();
        }
    }
}

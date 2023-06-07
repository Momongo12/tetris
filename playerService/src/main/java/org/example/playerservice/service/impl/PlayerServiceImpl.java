package org.example.playerservice.service.impl;

import org.example.playerservice.exception.PlayerNotFoundException;
import org.example.playerservice.model.dto.PlayerDto;
import org.example.playerservice.model.entity.Player;
import org.example.playerservice.model.entity.PlayerStatistic;
import org.example.playerservice.repository.PlayerRepository;
import org.example.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    public PlayerDto getPlayerDtoByEmailPlayer(String email) throws PlayerNotFoundException {
        Optional<Player> playerOptional = playerRepository.findByEmail(email);

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();
            PlayerStatistic playerStat = player.getPlayerStatistic();
            if (playerStat == null) {
                playerStat = new PlayerStatistic(player.getPlayerId());
                playerRepository.save(player);
            }
            PlayerDto playerDto = new PlayerDto(player.getPlayerId(), player.getName(), player.getDateOfRegistration(), playerStat.getNumberOfGames(),
                    playerStat.getMaxScore(), playerStat.getMaxLines(), playerStat.getMaxLevel(), playerStat.getAverageScore());
            System.out.println(playerDto);
            return playerDto;
        }else {
            throw new PlayerNotFoundException();
        }
    }
}

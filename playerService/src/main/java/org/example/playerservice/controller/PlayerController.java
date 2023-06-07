package org.example.playerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.playerservice.exception.PlayerNotFoundException;
import org.example.playerservice.model.dto.PlayerDto;
import org.example.playerservice.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
public class PlayerController {

    @Autowired
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/player/{emailPlayer}")
    public ResponseEntity<String> getPlayer(@PathVariable String emailPlayer) {
        try {
            PlayerDto playerDto = playerService.getPlayerDtoByEmailPlayer(emailPlayer);
            return ResponseEntity.ok().body(playerDto.serialize());
        } catch (PlayerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

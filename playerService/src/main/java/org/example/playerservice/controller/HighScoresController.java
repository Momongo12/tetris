package org.example.playerservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.playerservice.model.dto.PlayerDto;
import org.example.playerservice.model.dto.ScoreEntryDto;
import org.example.playerservice.service.HighScoresService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class HighScoresController {

    @Autowired
    private final HighScoresService highScoresService;

    public HighScoresController(HighScoresService highScoresService) {
        this.highScoresService = highScoresService;
    }

    @GetMapping("/high_scores")
    public ResponseEntity<String> getHighScores() {
        List<ScoreEntryDto> scoreEntryDtos = highScoresService.getHighScores();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(scoreEntryDtos);

            return new ResponseEntity<>(json, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>("Error converting to JSON", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

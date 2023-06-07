package org.example.playerservice.model.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.Date;

@Data
public class PlayerDto {
    private Long playerIdInDB;
    private String name;
    private Date dateOfRegistation;
    private int numberOfGames;
    private int maxScore;
    private int maxLines;
    private int maxLevel;
    private int averageScore;

    public PlayerDto(Long playerIdInDB, String name, Date dateOfRegistation, int numberOfGames, int maxScore, int maxLines, int maxLevel, int averageScore) {
        this.playerIdInDB = playerIdInDB;
        this.name = name;
        this.dateOfRegistation = dateOfRegistation;
        this.numberOfGames = numberOfGames;
        this.maxScore = maxScore;
        this.maxLines = maxLines;
        this.maxLevel = maxLevel;
        this.averageScore = averageScore;
    }

    public String serialize() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this);
    }

    public static PlayerDto deserialize(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, PlayerDto.class);
    }
}

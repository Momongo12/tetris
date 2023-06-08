package org.example.playerservice.model.dto;


import lombok.Data;

@Data
public class ScoreEntryDto {
    private String username;
    private int score;
    private int level;
    private int lines;

    public ScoreEntryDto(String username, int score, int level, int lines) {
        this.username = username;
        this.score = score;
        this.level = level;
        this.lines = lines;
    }
}
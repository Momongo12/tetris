package com.example.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.UUID;

@Data
public class PvPGameSession {
    public static final int ROWS = 20;
    public static final int COLUMNS = 10;

    private String id;
    private String player1;
    private String player2;
    private int scorePlayer1;
    private int scorePlayer2;
    private int levelPlayer1;
    private int levelPlayer2;
    private int linesClearedPlayer1;
    private int linesClearedPlayer2;
    private Square[][] gameMatrixPlayer1;
    private Square[][] gameMatrixPlayer2;

    public PvPGameSession() {
        this.id = UUID.randomUUID().toString();
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.levelPlayer1 = 1;
        this.levelPlayer2 = 1;
        this.linesClearedPlayer1 = 0;
        this.linesClearedPlayer2 = 0;
        this.gameMatrixPlayer1 = new Square[ROWS][COLUMNS];
        this.gameMatrixPlayer2 = new Square[ROWS][COLUMNS];
    }

    public String serialize() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PvPGameSession deserialize(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, PvPGameSession.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

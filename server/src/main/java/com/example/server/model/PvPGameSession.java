package com.example.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class PvPGameSession {
    private String sessionId;
    private int scorePlayer1;
    private int scorePlayer2;
    private int levelPlayer1;
    private int levelPlayer2;
    private int linesClearedPlayer1;
    private int linesClearedPlayer2;
    private int gameSpeed;
    private boolean isGameOver;
    private ArrayList<Square[]> gameMatrixPlayer1;
    private ArrayList<Square[]> gameMatrixPlayer2;
    private Tetromino tetrominoPlayer1;
    private Tetromino tetrominoPlayer2;
    private Tetromino nextTetrominoPlayer1;
    private Tetromino nextTetrominoPlayer2;
    private Tetromino holdTetrominoPlayer1;
    private Tetromino holdTetrominoPlayer2;

    public PvPGameSession() {
        this.sessionId = UUID.randomUUID().toString();
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.levelPlayer1 = 1;
        this.levelPlayer2 = 1;
        this.linesClearedPlayer1 = 0;
        this.linesClearedPlayer2 = 0;
        this.gameSpeed = 1000;
        this.isGameOver = false;
    }

    public void increaseScorePlayer1(int value){
        scorePlayer1 += value;
    }
    public void increaseScorePlayer2(int value){
        scorePlayer2 += value;
    }
    public void increaseLevelPlayer1(int value){
        levelPlayer1 += value;
    }
    public void increaseLevelPlayer2(int value) {
        levelPlayer2 += value;
    }
    public void increaseLinesClearedPlayer1(int value) {
        linesClearedPlayer1 += value;
    }
    public void increaseLinesClearedPlayer2(int value) {
        linesClearedPlayer2 += value;
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

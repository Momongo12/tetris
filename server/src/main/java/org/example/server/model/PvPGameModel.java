package org.example.server.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.UUID;

@Data
public class PvPGameModel {
    private String sessionId;
    private final String player1SessionId;
    private final String player2SessionId;
    private int scorePlayer1;
    private int scorePlayer2;
    private int levelPlayer1;
    private int levelPlayer2;
    private int linesClearedPlayer1;
    private int linesClearedPlayer2;
    private int gameSpeed;
    private boolean isGameOverPlayer1;
    private boolean isGameOverPlayer2;
    private ArrayList<Square[]> gameMatrixPlayer1;
    private ArrayList<Square[]> gameMatrixPlayer2;
    private Tetromino tetrominoPlayer1;
    private Tetromino tetrominoPlayer2;
    private Tetromino nextTetrominoPlayer1;
    private Tetromino nextTetrominoPlayer2;
    private Tetromino holdTetrominoPlayer1;
    private Tetromino holdTetrominoPlayer2;

    public PvPGameModel(String player1SessionId, String player2SessionId) {
        this.player1SessionId = player1SessionId;
        this.player2SessionId = player2SessionId;
        this.sessionId = UUID.randomUUID().toString();
        this.scorePlayer1 = 0;
        this.scorePlayer2 = 0;
        this.levelPlayer1 = 1;
        this.levelPlayer2 = 1;
        this.linesClearedPlayer1 = 0;
        this.linesClearedPlayer2 = 0;
        this.gameSpeed = 1000;
        this.isGameOverPlayer1 = false;
        this.isGameOverPlayer2 = false;
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

    public static PvPGameModel deserialize(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, PvPGameModel.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package tetris.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import tetris.view.Square;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author denMoskvin
 * @version 1.0
 */
@Data
@Log4j2
public class PvPGameSession {
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
    public PvPGameSession() {
        this.player1SessionId = null;
        this.player2SessionId = null;
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
            log.error(e.getStackTrace());
        }
        return null;
    }

    public static PvPGameSession deserialize(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, PvPGameSession.class);
        } catch (JsonProcessingException e) {
            log.error(e.getStackTrace());
        }
        return null;
    }
}

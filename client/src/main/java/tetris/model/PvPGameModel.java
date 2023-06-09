package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import tetris.controller.WebSocketClient;
import tetris.resource.ResourceManager;
import tetris.view.*;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static tetris.util.TetrisConstants.*;

/**
 * @author denMoskvin
 * @version 1.0
 */
@Log4j2
public class PvPGameModel implements GameModel {

    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    private final Color boardColor = new Color(20, 40, 60);
    private Clip moveSound, rotateSound, dropSound;
    private final GameLauncher gameLauncher;
    private PvPGameSession pvPGameSession;
    private final WebSocketClient webSocketClient;

    private GamePanel gamePanel;

    public PvPGameModel(GameLauncher gameLauncher, PvPGameSession pvPGameSession) {
        this.webSocketClient = gameLauncher.getWebSocketClient();
        this.pvPGameSession = pvPGameSession;
        pvPGameSession.setGameMatrixPlayer1(createBoard());
        pvPGameSession.setGameMatrixPlayer2(createBoard());
        this.gameLauncher = gameLauncher;
        initSounds();
    }

    private ArrayList<Square[]> createBoard() {
        ArrayList<Square[]> board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(i, new Square[COLUMNS]);
            for (int j = 0; j < COLUMNS; j++) {
                int x = (j * BLOCK_SIZE);
                int y = (i * BLOCK_SIZE);
                board.get(i)[j] = new Square(x, y, i, j, boardColor);
            }
        }
        return board;
    }

    public void initSounds(){
        try{
            moveSound = ResourceManager.getSound("moveSound.wav");
            rotateSound = ResourceManager.getSound("rotateSound.wav");
            dropSound = ResourceManager.getSound("dropSound.wav");

            LineListener listenerForProcessingSoundEnd = event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    dropSound.setFramePosition(0);
                }
            };

            //process the end of the clip
            moveSound.addLineListener(listenerForProcessingSoundEnd);
            rotateSound.addLineListener(listenerForProcessingSoundEnd);
            dropSound.addLineListener(listenerForProcessingSoundEnd);

            //volume level setting
            ((FloatControl) moveSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);
            ((FloatControl) rotateSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);
            ((FloatControl) dropSound.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-20f);

            log.debug("Game sounds loaded");

        } catch (Exception ex){
            log.error("Game sound initialization error", ex);
        }

    }


    public void startGame() {
        SwingWorker<Void, Void> worker1 = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (!pvPGameSession.isGameOverPlayer1()) {
                    try {
                        Thread.sleep(pvPGameSession.getGameSpeed() / 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gamePanel.repaint();
                }
                endGame(pvPGameSession.getPlayer1SessionId());
                return null;
            }
        };
        SwingWorker<Void, Void> worker2 = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                while (!pvPGameSession.isGameOverPlayer2()) {
                    try {
                        Thread.sleep(pvPGameSession.getGameSpeed() / 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gamePanel.repaint();
                }
                endGame(pvPGameSession.getPlayer2SessionId());
                return null;
            }
        };

        worker1.execute();
        worker2.execute();
    }

    public void pauseGame() {

    }

    private void endGame(String playerSessionId) {
        gamePanel.displayGameOverPanel(playerSessionId);
        if (playerSessionId.equals(getOwnSessionId())){
            System.out.println(gameLauncher.getCurrentPlayer().getName() + getScore(playerSessionId) + getLines(playerSessionId) + getLevel(playerSessionId));
            gameLauncher.getNavigationPanel().getPlayOrPauseButton().doClick();
            gameLauncher.updateStatisticPlayer(getScore(playerSessionId), getLines(playerSessionId), getLevel(playerSessionId));
        }

    }

    public void moveTetromino(int direction) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode message = objectMapper.createObjectNode();
            message.put("eventType", "MoveTetromino");
            message.put("direction", direction);
            message.put("sessionId", pvPGameSession.getSessionId());

            String jsonMessage = objectMapper.writeValueAsString(message);

            webSocketClient.sendMessage(jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void rotateTetromino() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode message = objectMapper.createObjectNode();
            message.put("eventType", "RotateTetromino");
            message.put("sessionId", pvPGameSession.getSessionId());

            String jsonMessage = objectMapper.writeValueAsString(message);

            webSocketClient.sendMessage(jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void holdTetromino() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode message = objectMapper.createObjectNode();
            message.put("eventType", "HoldTetromino");
            message.put("sessionId", pvPGameSession.getSessionId());

            String jsonMessage = objectMapper.writeValueAsString(message);

            webSocketClient.sendMessage(jsonMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean getGameStatus() {
        return true;
    }

    public void setGamePanel(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public String getOwnSessionId() {
        if (webSocketClient.getSessionId().equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getPlayer1SessionId();
        }else {
            return pvPGameSession.getPlayer2SessionId();
        }
    }

    public String getOpponentSessionId() {
        if (webSocketClient.getSessionId().equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getPlayer2SessionId();
        }else {
            return pvPGameSession.getPlayer1SessionId();
        }
    }

    public int getLines(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getLinesClearedPlayer1();
        }else {
            return pvPGameSession.getLinesClearedPlayer2();
        }
    }

    public int getScore(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getScorePlayer1();
        }else {
            return pvPGameSession.getScorePlayer2();
        }
    }

    public int getLevel(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getLevelPlayer1();
        }else {
            return pvPGameSession.getLevelPlayer2();
        }
    }

    public ArrayList<Square[]> getBoard(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getGameMatrixPlayer1();
        }else {
            return pvPGameSession.getGameMatrixPlayer2();
        }
    }

    public Tetromino getTetromino(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getTetrominoPlayer1();
        }else {
            return pvPGameSession.getTetrominoPlayer2();
        }
    }

    public Tetromino getNextTetromino(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getNextTetrominoPlayer1();
        }else {
            return pvPGameSession.getNextTetrominoPlayer2();
        }
    }

    public Tetromino getHoldTetromino(String playerSessionId) {
        if (playerSessionId.equals(pvPGameSession.getPlayer1SessionId())){
            return pvPGameSession.getHoldTetrominoPlayer1();
        }else {
            return pvPGameSession.getHoldTetrominoPlayer2();
        }
    }

    public void setPvPGameSession(PvPGameSession pvPGameSession) {
        this.pvPGameSession = pvPGameSession;
    }

    public PvPGameSession getPvPGameSession() {
        return pvPGameSession;
    }
}

package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.Logger;
import tetris.controller.WebSocketClient;
import tetris.logger.MyLoggerFactory;
import tetris.resource.ResourceManager;
import tetris.view.*;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.awt.*;
import java.util.ArrayList;

import static tetris.util.TetrisConstants.*;

public class PvPGameModel implements GameModel {
    private static final Logger LOGGER = MyLoggerFactory.getLogger(SoloGameModel.class);

    public static final int ROWS = 20;
    public static final int COLUMNS = 10;
    private final Color boardColor = new Color(20, 40, 60);
    private Clip moveSound, rotateSound, dropSound;
    private final GameLauncher gameLauncher;
    private PvPGameSession pvPGameSession;
    private final WebSocketClient webSocketClient;

    private GamePanel gamePanel;
    private InfoPanel infoPanelPlayer1;
    private InfoPanel infoPanelPlayer2;
    private GameMatrix gameMatrixPlayer1;
    private GameMatrix gameMatrixPlayer2;
    private NextShapePanel nextShapePanelPlayer1;
    private NextShapePanel nextShapePanelPlayer2;
    private HoldPanel holdPanelPlayer1;
    private HoldPanel holdPanelPlayer2;
    private LevelPanel levelPanelPlayer1;
    private LevelPanel levelPanelPlayer2;

    public PvPGameModel(GameLauncher gameLauncher) {
        this.pvPGameSession = new PvPGameSession();
        initBoard(pvPGameSession.getGameMatrixPlayer1());
        initBoard(pvPGameSession.getGameMatrixPlayer2());
        this.gameLauncher = gameLauncher;
        this.webSocketClient = gameLauncher.getWebSocketClient();
        initSounds();
    }

    private void initBoard(ArrayList<Square[]> board) {
        board = new ArrayList<>();
        for (int i = 0; i < ROWS; i++) {
            board.add(i, new Square[COLUMNS]);
            for (int j = 0; j < COLUMNS; j++) {
                int x = (j * BLOCK_SIZE);
                int y = (i * BLOCK_SIZE);
                board.get(i)[j] = new Square(x, y, i, j, boardColor);
            }
        }
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

            LOGGER.debug("Game sounds loaded");

        } catch (Exception ex){
            LOGGER.error("Game sound initialization error", ex);
        }

    }


    public void startGame() {
        while (!pvPGameSession.isGameOverPlayer1() || !pvPGameSession.isGameOverPlayer2()) {
            try {
                Thread.sleep(pvPGameSession.getGameSpeed() / 2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            gamePanel.repaint();
        }
    }

    public void pauseGame() {

    }

    public void moveTetromino(int direction) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode message = objectMapper.createObjectNode();
            message.put("eventType", "moveTetromino");
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
            message.put("eventType", "rotateTetromino");
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
            message.put("eventType", "holdTetromino");
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

    public void addInfoPanel(InfoPanel infoPanel) {
        if (infoPanelPlayer1 == null) {
            infoPanelPlayer1 = infoPanel;
        }else {
            infoPanelPlayer2 = infoPanel;
        }
    }


    public void addGameMatrix(GameMatrix gameMatrix) {
        if (gameMatrixPlayer1 == null) {
            gameMatrixPlayer1 = gameMatrix;
        }else {
            gameMatrixPlayer2 = gameMatrix;
        }
    }

    public void addNextShapePanel(NextShapePanel nextShapePanel) {
        if (nextShapePanelPlayer1 == null) {
            nextShapePanelPlayer1 = nextShapePanel;
        }else {
            nextShapePanelPlayer2 = nextShapePanel;
        }
    }

    public void addHoldPanel(HoldPanel holdPanel) {
        if (holdPanelPlayer1 == null) {
            holdPanelPlayer1 = holdPanel;
        }else {
            holdPanelPlayer2 = holdPanel;
        }
    }

    public void addLevelPanel(LevelPanel levelPanel) {
        if (levelPanelPlayer1 == null) {
            levelPanelPlayer1 = levelPanel;
        }else {
            levelPanelPlayer2 = levelPanel;
        }
    }


    public int getLines(InfoPanel infoPanel) {
        if (infoPanel == infoPanelPlayer1){
            return pvPGameSession.getLinesClearedPlayer1();
        }else {
            return pvPGameSession.getLinesClearedPlayer2();
        }
    }

    public int getScore(InfoPanel infoPanel) {
        if (infoPanel == infoPanelPlayer1) {
            return pvPGameSession.getScorePlayer1();
        }else {
            return pvPGameSession.getScorePlayer2();
        }
    }

    public int getLevel(LevelPanel levelPanel) {
        if (levelPanel == levelPanelPlayer1) {
            return pvPGameSession.getLevelPlayer1();
        }else {
            return pvPGameSession.getLevelPlayer2();
        }
    }

    public ArrayList<Square[]> getBoard(GameMatrix gameMatrix) {
        if (gameMatrix == gameMatrixPlayer1) {
            return pvPGameSession.getGameMatrixPlayer1();
        }else {
            return pvPGameSession.getGameMatrixPlayer2();
        }
    }

    public Tetromino getTetromino(GameMatrix gameMatrix) {
        if (gameMatrix == gameMatrixPlayer1) {
            return pvPGameSession.getTetrominoPlayer1();
        }else {
            return pvPGameSession.getTetrominoPlayer2();
        }
    }

    public Tetromino getNextTetromino(NextShapePanel nextShapePanel) {
        if (nextShapePanel == nextShapePanelPlayer1) {
            return pvPGameSession.getNextTetrominoPlayer1();
        }else {
            return pvPGameSession.getNextTetrominoPlayer2();
        }
    }

    public Tetromino getHoldTetromino(HoldPanel holdPanel) {
        if (holdPanel == holdPanelPlayer1) {
            return pvPGameSession.getHoldTetrominoPlayer1();
        }else {
            return pvPGameSession.getHoldTetrominoPlayer2();
        }
    }

    public void setPvPGameSession(PvPGameSession pvPGameSession) {
        this.pvPGameSession = pvPGameSession;
    }
}

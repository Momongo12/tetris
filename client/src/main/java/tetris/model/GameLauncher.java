package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.logging.log4j.Logger;
import tetris.controller.LauncherController;
import tetris.controller.TetrominoController;
import tetris.controller.WebSocketClient;
import tetris.dataAccessLayer.HighScoreDataAccessObject;
import tetris.dataAccessLayer.PlayerStatisticsTableDataAccessObject;
import tetris.logger.MyLoggerFactory;
import tetris.resource.ResourceManager;
import tetris.view.GamePanel;
import tetris.view.LauncherPreview;
import tetris.view.LauncherView;
import tetris.view.NavigationPanel;

import javax.sound.sampled.*;
import javax.swing.*;

@Data
public class GameLauncher {
    private NavigationPanel navigationPanel;
    private LauncherController launcherController;
    private LauncherView launcherView;
    private TetrominoController tetrominoController;
    private GameModel gameModel;
    private WebSocketClient webSocketClient;
    private Player currentPlayer;
    private GamePanel gamePanel;

    private boolean isGameActive = false;
    private SwingWorker<Void, Void> worker;

    private boolean backgroundMusicIsDisable = true;

    private Clip[] arrayOfSounds;
    private int soundIndex;

    private static final Logger LOGGER = MyLoggerFactory.getLogger(GameLauncher.class);

    public GameLauncher(){
        gameModel = new SoloGameModel(this);
        tetrominoController = new TetrominoController(gameModel);

        new LauncherPreview(this);
        launcherController = new LauncherController(this);
        launcherView = new LauncherView(this, launcherController);
        launcherView.addKeyListener(tetrominoController);

        initBackgroundSounds();
        LOGGER.info("Launcher started");
    }

    private void initBackgroundSounds(){
        try {
            arrayOfSounds = ResourceManager.getAllBackgroundSounds();
            for (int i = 0; i < arrayOfSounds.length; i++) {
                Clip clip = arrayOfSounds[i];
                ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-10f);
                clip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP){
                        clip.setFramePosition(0);
                        if (!backgroundMusicIsDisable){
                            clip.start();
                        }
                    }
                });
            }
            soundIndex = 0;
            LOGGER.debug("background sounds loaded");
        }catch (Exception ex){
            LOGGER.error("Init background sounds error", ex);
        }
    }

    public void startGame(){
        if (webSocketClient != null &&!isGameActive) {
            doRequestToStartGame();
            return;
        }
        if (backgroundMusicIsDisable) {
            arrayOfSounds[soundIndex].start();
            backgroundMusicIsDisable = false;
        }
        worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground(){
                gameModel.startGame();
                return null;
            }
        };
        worker.execute();
        LOGGER.info("Game started");
    }

    public void connectToServer() {
        if (webSocketClient == null) {
            System.out.println("попытка подключения к серверу");
            webSocketClient = new WebSocketClient(this);
            webSocketClient.connect("ws://localhost:8080/game");
        }
    }

    private void doRequestToStartGame() {
        if (webSocketClient != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode message = objectMapper.createObjectNode();
                message.put("eventType", "startGame");
                message.put("sessionId", ((PvPGameModel) gameModel).getPvPGameSession().getSessionId());

                String jsonMessage = objectMapper.writeValueAsString(message);

                webSocketClient.sendMessage(jsonMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


//    public void destroyWebSocketClient() {
//        webSocketClient = null;
//    }

    public void displayPvPGameField(PvPGameSession pvPGameSession) {
        if (webSocketClient != null) {
            gameModel = new PvPGameModel(this, pvPGameSession);
            tetrominoController.setGameModel(gameModel);
            launcherView.displayPvPGameMode(gameModel);
        }
    }

    public void findPvPGameSession() {
        if (webSocketClient != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode message = objectMapper.createObjectNode();
                message.put("eventType", "findPvPGameSession");

                String jsonMessage = objectMapper.writeValueAsString(message);

                webSocketClient.sendMessage(jsonMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePvPGameSession(PvPGameSession gameSession) {
        if (webSocketClient != null){
            ((  PvPGameModel) gameModel).setPvPGameSession(gameSession);
        }
    }

    public void stopGame(){
        if (worker != null) {
            arrayOfSounds[soundIndex].stop();
            backgroundMusicIsDisable = true;
            gameModel.pauseGame();
            worker.cancel(false);
        }
        LOGGER.info("Game stopped");
    }


    public boolean isBackgroundMusicDisabled(){
        return backgroundMusicIsDisable;
    }

    public void changeStateOfBackgroundMusic(){
        if (isBackgroundMusicDisabled()){
            ((FloatControl) arrayOfSounds[soundIndex].getControl(FloatControl.Type.MASTER_GAIN)).setValue(-10f);
        }else {
            ((FloatControl) arrayOfSounds[soundIndex].getControl(FloatControl.Type.MASTER_GAIN)).setValue(-80f);
        }
        backgroundMusicIsDisable = !backgroundMusicIsDisable;
    }

    public void updateBackgroundSound(){
        if (arrayOfSounds[soundIndex].isRunning()) {
            arrayOfSounds[soundIndex].stop();
        }
        soundIndex = (soundIndex + 1) % arrayOfSounds.length;

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                playPreviewSound(arrayOfSounds[soundIndex]);
                return null;
            }
        };

        worker.execute();

        LOGGER.info("Background music has been updated");
    }

    private void playPreviewSound(Clip clip){
        try {
            clip.start();
            Thread.sleep(7000);
            clip.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPlayer(String username){
        currentPlayer = PlayerStatisticsTableDataAccessObject.getPlayerStatistics(username);
        if (currentPlayer == null){
            currentPlayer = new Player("Unknown", 1);
        }
        LOGGER.info("Player log in");
    }

    public void updateStatisticPlayer(int currentScore, int currentLines, int currentLevel){
        currentPlayer.updateStatisticPlayer(currentScore, currentLines, currentLevel);
        PlayerStatisticsTableDataAccessObject.updatePlayerStatisticsInDB(currentPlayer);
        HighScoreDataAccessObject.addHighScoreDataToDB(currentPlayer);
    }

    public void setNavigationPanel(NavigationPanel navigationPanel){
        this.navigationPanel = navigationPanel;
    }

    public NavigationPanel getNavigationPanel() { return navigationPanel; }

    public SoloGameModel getSoloGameModel(){
        return (SoloGameModel) gameModel;
    }

    public LauncherView getLauncherView(){
        return launcherView;
    }

    public Player getcurrentPlayer() {
        return currentPlayer;
    }
}
package tetris.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import tetris.controller.LauncherController;
import tetris.controller.TetrominoController;
import tetris.controller.WebSocketClient;
import tetris.resource.ResourceManager;
import tetris.service.AuthService;
import tetris.service.HighScoresService;
import tetris.service.PlayerStatsService;
import tetris.service.impl.AuthServiceImpl;
import tetris.service.impl.HighScoresServiceImpl;
import tetris.service.impl.PlayerStatsServiceImpl;
import tetris.view.GamePanel;
import tetris.view.LauncherPreview;
import tetris.view.LauncherView;
import tetris.view.NavigationPanel;

import javax.sound.sampled.*;
import javax.swing.*;

/**
 * @author denMoskvin
 * @version 1.0
 */
@Data
@Log4j2
public class GameLauncher {
    private NavigationPanel navigationPanel;
    private LauncherController launcherController;
    private LauncherView launcherView;
    private TetrominoController tetrominoController;
    private GameModel gameModel;
    private WebSocketClient webSocketClient;
    private Player currentPlayer;
    private GamePanel gamePanel;
    private PlayerStatsService playerStatsService;
    private AuthService authService;
    private HighScoresService highScoresService;
    private boolean isGameActive = false;
    private SwingWorker<Void, Void> worker;

    private boolean backgroundMusicIsDisable = true;

    private Clip[] arrayOfSounds;
    private int soundIndex;

    public GameLauncher(){
        gameModel = new SoloGameModel(this);
        tetrominoController = new TetrominoController(gameModel);

        initServices();

        launcherController = new LauncherController(this, authService, playerStatsService, highScoresService);
        launcherView = new LauncherView(this, launcherController);
        launcherView.addKeyListener(tetrominoController);

        new LauncherPreview(this);

        initBackgroundSounds();
        log.info("Launcher started");
    }

    private void initServices() {
        this.authService = new AuthServiceImpl();
        this.playerStatsService = new PlayerStatsServiceImpl();
        this.highScoresService = new HighScoresServiceImpl();
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
            log.debug("background sounds loaded");
        }catch (Exception ex){
            log.error("Init background sounds error", ex);
        }
    }

    public void startGame(){
        if (webSocketClient != null && !isGameActive) {
            isGameActive = true;
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
        log.info("Game started");
    }

    public void restartGame(){
        if (gameModel instanceof SoloGameModel) {
            ((SoloGameModel) gameModel).refreshGameData();
            launcherView.displayChooseGameModePanel();
        }else {
            if (isGameActive) {
                webSocketClient.close();
            }
            isGameActive = false;
            webSocketClient = null;
            connectToServer();
            findPvPGameSession();
        }
    }

    public void connectToServer() {
        if (webSocketClient == null) {
            log.info("Try connect to server");
            webSocketClient = new WebSocketClient(this);
            webSocketClient.connect("ws://localhost:8082/game");
        }
    }

    private void doRequestToStartGame() {
        if (webSocketClient != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode message = objectMapper.createObjectNode();
                message.put("eventType", "StartGame");
                message.put("sessionId", ((PvPGameModel) gameModel).getPvPGameSession().getSessionId());

                String jsonMessage = objectMapper.writeValueAsString(message);

                webSocketClient.sendMessage(jsonMessage);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }


    public void destroyWebSocketClient() {
        webSocketClient = null;
    }

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
                message.put("eventType", "FindPvPGameSession");

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
        log.info("Game stopped");
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

        log.info("Background music has been updated");
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

    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
    }

    public void updateStatisticPlayer(int currentScore, int currentLines, int currentLevel){
        currentPlayer.updateStatistic(currentScore, currentLines, currentLevel);
        playerStatsService.updateStatisticPlayer(currentPlayer);
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

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public HighScore getHighScore() {
        return highScoresService.getHighScores();
    }
}
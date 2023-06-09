package tetris.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import tetris.model.GameLauncher;
import tetris.model.PvPGameSession;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@Log4j2
@ClientEndpoint
public class WebSocketClient implements MessageHandler.Whole<String>{
    private GameLauncher gameLauncher;
    private Session session;

    private String serverSideSessionId;

    public WebSocketClient(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("WebSocket connection established.");
    }

    @OnMessage
    public void onMessage(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String eventType = jsonNode.get("eventType").asText();

        if (eventType.equals("PvPGameSessionCreated")){
            this.serverSideSessionId = jsonNode.get("serverSideSessionId").asText();

            PvPGameSession pvPGameSession = PvPGameSession.deserialize(jsonNode.get("pvPGameModel").asText());
            gameLauncher.displayPvPGameField(pvPGameSession);
            gameLauncher.updatePvPGameSession(pvPGameSession);
        }else if (eventType.equals("gameStarted")){
            PvPGameSession pvPGameSession = PvPGameSession.deserialize(jsonNode.get("pvPGameModel").asText());
            gameLauncher.updatePvPGameSession(pvPGameSession);

            /*
            If the game is active false, then we have not pressed play,
            so we need to update the game status icon and so we press play,
            and if true, then we have already pressed this button and we just need to run the game loop
             */
            if (gameLauncher.isGameActive()) {
                gameLauncher.startGame();
            }else {
                gameLauncher.setGameActive(true);
                gameLauncher.getNavigationPanel().getPlayOrPauseButton().doClick();
            }
        }else if (eventType.equals("updateGameSession")){
            PvPGameSession pvPGameSession = PvPGameSession.deserialize(jsonNode.get("pvPGameModel").asText());
            gameLauncher.updatePvPGameSession(pvPGameSession);
        }
    }

    @OnClose
    public void onClose() {
        log.info("WebSocket connection closed.");
    }

    public void connect(String serverURI) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, URI.create(serverURI));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSessionId() {
        return serverSideSessionId;
    }
}


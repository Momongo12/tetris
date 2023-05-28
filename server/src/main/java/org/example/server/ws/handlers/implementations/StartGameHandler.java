package org.example.server.ws.handlers.implementations;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of the {@link PvPGameSessionHandler} interface.
 * This handler is responsible for starting the game in a PvPGameSession.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
public class StartGameHandler implements PvPGameSessionHandler {
    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    public StartGameHandler() {
    }

    /**
     * Handles the "startGame" event by starting the game in the PvPGameSession.
     */
    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        String sessionId = jsonNode.get("sessionId").asText();
        PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

        if (pvPGameSession != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("eventType", "gameStarted");
                objectNode.put("pvPGameModel", pvPGameSession.getPvPGameModel().serialize());

                String jsonMessage = objectMapper.writeValueAsString(objectNode);

                pvPGameSession.getPlayer1Session().sendMessage(new TextMessage(jsonMessage));
                pvPGameSession.getPlayer2Session().sendMessage(new TextMessage(jsonMessage));

                pvPGameSession.startGame();
            } catch (IOException e) {
                log.error("starting game error");
                log.error(e.getLocalizedMessage());
            }
        }else {
            log.error("starting game error");
        }
    }
}

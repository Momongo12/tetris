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

/**
 * Implementation of the {@link PvPGameSessionHandler} interface.
 * This handler is responsible for creating a PvPGameSession and sending the necessary messages to the players involved.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
public class FindPvPGameSessionHandler implements PvPGameSessionHandler {
    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    public FindPvPGameSessionHandler() {
    }

    /**
     * Handles the "findPvPGameSession" event by creating a PvPGameSession and sending necessary messages to the players.
     */
    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        PvPGameSession pvPGameSession =  pvPGameSessionMatcher.createPvPGameSession(playerSession);
        if (pvPGameSession != null) {
            log.info("pvPGameSession created");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ObjectNode objectNode = objectMapper.createObjectNode();
                objectNode.put("eventType", "PvPGameSessionCreated");
                objectNode.put("pvPGameModel", pvPGameSession.getPvPGameModel().serialize());
                objectNode.put("serverSideSessionId", pvPGameSession.getPlayer1Session().getId());

                String jsonMessage = objectMapper.writeValueAsString(objectNode);
                pvPGameSession.getPlayer1Session().sendMessage(new TextMessage(jsonMessage));

                objectNode.put("serverSideSessionId", pvPGameSession.getPlayer2Session().getId());
                jsonMessage = objectMapper.writeValueAsString(objectNode);
                pvPGameSession.getPlayer2Session().sendMessage(new TextMessage(jsonMessage));
            } catch (IOException e) {
                log.error("pvPGameSession finding error");
                log.error(e.getLocalizedMessage());
            }
        }else {
            log.warn("pvPGameSession finding error");
        }
    }
}

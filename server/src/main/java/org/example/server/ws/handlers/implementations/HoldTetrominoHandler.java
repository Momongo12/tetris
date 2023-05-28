package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

/**
 * Implementation of the {@link PvPGameSessionHandler} interface.
 * This handler is responsible for holding a tetromino in a PvPGameSession.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
public class HoldTetrominoHandler implements PvPGameSessionHandler {
    public HoldTetrominoHandler() {
    }

    /**
     * Handles the "holdTetromino" event by holding a tetromino in the PvPGameSession.
     */
    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        String sessionId = jsonNode.get("sessionId").asText();
        PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

        pvPGameSession.holdTetromino(playerSession);
    }
}

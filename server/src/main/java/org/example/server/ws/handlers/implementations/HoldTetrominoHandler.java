package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

public class HoldTetrominoHandler implements PvPGameSessionHandler {
    public HoldTetrominoHandler() {
    }

    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        String sessionId = jsonNode.get("sessionId").asText();
        PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

        pvPGameSession.holdTetromino(playerSession);
    }
}

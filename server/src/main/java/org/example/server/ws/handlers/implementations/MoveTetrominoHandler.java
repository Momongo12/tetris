package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.model.PvPGameModel;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

public class MoveTetrominoHandler implements PvPGameSessionHandler {
    public MoveTetrominoHandler() {
    }

    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        String sessionId = jsonNode.get("sessionId").asText();
        String direction = jsonNode.get("direction").asText();
        PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

        PvPGameModel pvPGameModel = pvPGameSession.getPvPGameModel();
        if (playerSession == pvPGameSession.getPlayer1Session()) {
            pvPGameSession.moveTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1(), Integer.parseInt(direction), playerSession);
        }else {
            pvPGameSession.moveTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2(), Integer.parseInt(direction), playerSession);
        }
    }
}

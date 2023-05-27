package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.model.PvPGameModel;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

public class RotateTetrominoHandler implements PvPGameSessionHandler {
    public RotateTetrominoHandler() {
    }

    @Override
    public void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode) {
        String sessionId = jsonNode.get("sessionId").asText();
        PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

        PvPGameModel pvPGameModel = pvPGameSession.getPvPGameModel();
        if (playerSession == pvPGameSession.getPlayer1Session()) {
            pvPGameSession.rotateTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1());
        }else {
            pvPGameSession.rotateTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2());
        }
    }
}

package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.server.model.PvPGameModel;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

/**
 * Implementation of the {@link PvPGameSessionHandler} interface.
 * This handler is responsible for rotating a tetromino in a PvPGameSession.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
public class RotateTetrominoHandler implements PvPGameSessionHandler {
    public RotateTetrominoHandler() {
    }

    /**
     * Handles the "rotateTetromino" event by rotating a tetromino in the PvPGameSession.
     */
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

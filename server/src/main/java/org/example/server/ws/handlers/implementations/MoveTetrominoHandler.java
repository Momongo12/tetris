package org.example.server.ws.handlers.implementations;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.server.model.PvPGameModel;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.WebSocketSession;

/**
 * Implementation of the {@link PvPGameSessionHandler} interface.
 * This handler is responsible for moving a tetromino in a PvPGameSession.
 *
 * @version 1.0
 * @author Denis Moskvin
 */
public class MoveTetrominoHandler implements PvPGameSessionHandler {
    public MoveTetrominoHandler() {
    }

    /**
     * Handles the "moveTetromino" event by moving a tetromino in the PvPGameSession.
     */
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

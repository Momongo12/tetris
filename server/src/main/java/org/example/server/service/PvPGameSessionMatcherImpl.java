package org.example.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 Implementation of the {@link PvPGameSessionMatcher} interface.

 * @version 1.0
 * @author Denis Moskvin
 */
@Service
public class PvPGameSessionMatcherImpl implements PvPGameSessionMatcher {
    private List<WebSocketSession> waitingPlayers = new ArrayList<>();
    private Map<String, PvPGameSession> pvPGameSessions = new ConcurrentHashMap<>();

    /**
     * Creates a PvP game session with the specified player session.
     *
     * @param playerSession The WebSocket session of the player.
     * @return The created PvP game session, or {@code null} if it couldn't be created.
     */
    public PvPGameSession createPvPGameSession(WebSocketSession playerSession) {
        if (waitingPlayers.contains(playerSession)) {
            if (waitingPlayers.size() >= 2) {
                waitingPlayers.remove(playerSession);
                WebSocketSession player2Session = waitingPlayers.remove(0);

                PvPGameSession pvPGameSession = new PvPGameSession(playerSession, player2Session);
                pvPGameSessions.put(pvPGameSession.getPvPGameModel().getSessionId(), pvPGameSession);

                return pvPGameSession;
            }
        }
        return null;
    }
    /**
     * Adds a player session to the list of waiting players.
     * @param session The WebSocket session of the player to add.
     */
    public void addWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.add(session);
    }

    public void removeWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.remove(session);
    }

    /**
     * Retrieves the PvP game session with the specified session ID.
     * @param sessionId The session ID of the PvP game session.
     * @return The PvP game session with the given session ID, or {@code null} if not found.
     */
    public PvPGameSession getPvPGameSession(String sessionId) {
        return pvPGameSessions.get(sessionId);
    }
}

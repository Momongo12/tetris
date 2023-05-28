package org.example.server.service;

import org.springframework.web.socket.WebSocketSession;

/**
 * @version 1.0
 * @author Denis Moskvin
 */
public interface PvPGameSessionMatcher {
    PvPGameSession createPvPGameSession(WebSocketSession playerSession);
    void addWaitingPlayerSession(WebSocketSession session);
    void removeWaitingPlayerSession(WebSocketSession session);

    PvPGameSession getPvPGameSession(String sessionId);
}

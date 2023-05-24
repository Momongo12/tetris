package org.example.server.service;

import org.springframework.web.socket.WebSocketSession;

public interface PvPGameSessionMatcher {
    void createPvPGameSession(WebSocketSession playerSession);
    void addWaitingPlayerSession(WebSocketSession session);
    void removeWaitingPlayerSession(WebSocketSession session);

    PvPGameSession getPvPGameSession(String sessionId);
}
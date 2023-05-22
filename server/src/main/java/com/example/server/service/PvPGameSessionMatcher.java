package com.example.server.service;

import com.example.server.model.PvPGameSession;
import org.springframework.web.socket.WebSocketSession;

public interface PvPGameSessionMatcher {
    PvPGameSession createPvPGameSession(WebSocketSession playerSession);
    void addWaitingPlayerSession(WebSocketSession session);
    void removeWaitingPlayerSession(WebSocketSession session);
}

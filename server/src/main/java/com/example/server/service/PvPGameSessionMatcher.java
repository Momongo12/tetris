package com.example.server.service;

import com.example.server.model.PvPGameModel;
import org.springframework.web.socket.WebSocketSession;

public interface PvPGameSessionMatcher {
    void createPvPGameSession(WebSocketSession playerSession);
    void addWaitingPlayerSession(WebSocketSession session);
    void removeWaitingPlayerSession(WebSocketSession session);

    PvPGameModel getPvPGameModel(String sessionId);
}

package org.example.server.service;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PvPGameSessionMatcherImpl implements PvPGameSessionMatcher {
    private List<WebSocketSession> waitingPlayers = new ArrayList<>();
    private Map<String, PvPGameSession> pvPGameSessions = new ConcurrentHashMap<>();

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
    public void addWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.add(session);
    }

    public void removeWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.remove(session);
    }

    public PvPGameSession getPvPGameSession(String sessionId) {
        return pvPGameSessions.get(sessionId);
    }
}

package com.example.server.service;

import com.example.server.model.PvPGameSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PvPGameSessionMatcherImpl implements PvPGameSessionMatcher{
    private Set<WebSocketSession> waitingPlayers = new HashSet<>();
    private Map<String, PvPGameSession> pvPGameSessions = new ConcurrentHashMap<>();

    public PvPGameSession createPvPGameSession(WebSocketSession playerSession) {
        if (!waitingPlayers.contains(playerSession)) {
            Optional<String> matchingKey = pvPGameSessions.keySet()
                    .stream().filter((s) -> s.contains(playerSession.getId()))
                    .findFirst();
            if (matchingKey.isPresent()) {
                return pvPGameSessions.get(matchingKey.get());
            }
        } //
        return null;
    }

    public void addWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.add(session);
    }

    public void removeWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.remove(session);
    }
}

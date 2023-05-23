package com.example.server.service;

import com.example.server.model.PvPGameModel;
import com.example.server.model.PvPGameSession;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PvPGameSessionMatcherImpl implements PvPGameSessionMatcher{
    private List<WebSocketSession> waitingPlayers = new ArrayList<>();
    private Map<String, PvPGameModel> pvPGameModels = new ConcurrentHashMap<>();

    public void createPvPGameSession(WebSocketSession playerSession) {
        if (waitingPlayers.contains(playerSession)) {
            waitingPlayers.remove(playerSession);
            WebSocketSession player2Session = waitingPlayers.remove(0);
            PvPGameModel pvPGameModel = new PvPGameModel(playerSession, player2Session);
            pvPGameModels.put(pvPGameModel.getPvPGameSession().getSessionId(), pvPGameModel);

            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(pvPGameModel::startGame);
        }
    }

    public void addWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.add(session);
    }

    public void removeWaitingPlayerSession(WebSocketSession session) {
        waitingPlayers.remove(session);
    }

    public PvPGameModel getPvPGameModel(String sessionId) {
        return pvPGameModels.get(sessionId);
    }
}

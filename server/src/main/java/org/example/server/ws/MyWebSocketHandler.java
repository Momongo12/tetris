package org.example.server.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.server.model.PvPGameModel;
import org.example.server.service.PvPGameSession;
import org.example.server.service.PvPGameSessionMatcher;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.ws.handlers.PvPGameSessionHandler;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyWebSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    private final PvPGameSessionMatcher pvPGameSessionMatcher;
    private Map<String, PvPGameSessionHandler> messageHandlers;

    public MyWebSocketHandler(PvPGameSessionMatcher pvPGameSessionMatcher) {
        this.pvPGameSessionMatcher = pvPGameSessionMatcher;
        try {
            messageHandlers = PvPGameSessionHandler.getMessageHandlersMap();
        }catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Добавляем подключенного игрока в список подключенных игроков
        pvPGameSessionMatcher.addWaitingPlayerSession(session);

        // Код, который выполняется при установке соединения с клиентом
        System.out.println("Player connected. Session ID: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession playerSession, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);

        String eventType = jsonNode.get("eventType").asText();

        PvPGameSessionHandler pvPGameSessionHandler = messageHandlers.get(eventType);

        if (pvPGameSessionHandler != null) {
            pvPGameSessionHandler.handle(playerSession, pvPGameSessionMatcher, jsonNode);
        }else {
            log.warn("Message type error");
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        // Код, выполняемый при возникновении ошибки связи
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // Код, выполняемый после закрытия соединения с клиентом
    }
}

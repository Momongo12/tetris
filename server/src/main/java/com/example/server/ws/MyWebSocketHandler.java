package com.example.server.ws;

import com.example.server.model.PvPGameSession;
import com.example.server.service.PvPGameSessionMatcher;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;


public class MyWebSocketHandler extends TextWebSocketHandler {
    private final PvPGameSessionMatcher pvPGameSessionMatcher;

    public MyWebSocketHandler(PvPGameSessionMatcher pvPGameSessionMatcher) {
        this.pvPGameSessionMatcher = pvPGameSessionMatcher;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Добавляем подключенного игрока в список подключенных игроков
        pvPGameSessionMatcher.addWaitingPlayerSession(session);

        // Код, который выполняется при установке соединения с клиентом
        System.out.println("Player connected. Session ID: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession playerSession, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();

        if (payload.startsWith("createSession")) {
            PvPGameSession gameSession = pvPGameSessionMatcher.createPvPGameSession(playerSession);

            // Отправка найденной игровой сессии клиенту
            playerSession.sendMessage(new TextMessage(gameSession.serialize()));
        } else {
//            // Получение данных из сообщения
//            ObjectMapper objectMapper = new ObjectMapper();
//            KeyEventData keyEventData = objectMapper.readValue(payload, KeyEventData.class);
//
//            // Извлечение информации из KeyEventData
//            String gameSessionId = keyEventData.getGameSessionId();
//            String playerId = keyEventData.getPlayerId();
//            String eventType = keyEventData.getEventType();
//
//            // Обработка события в зависимости от типа
//            if (eventType.equals("keyPressed")) {
//                handleKeyPressedEvent(gameSessionId, playerId);
//            } else if (eventType.equals("keyReleased")) {
//                handleKeyReleasedEvent(gameSessionId, playerId);
//            }
//
//            // Отправка обновления всем участникам игровой сессии
//            sendGameSessionUpdate(gameSessionId);
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

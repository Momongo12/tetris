package com.example.server.ws;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

public class MyWebSocketHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> connectedPlayers = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // Добавляем подключенного игрока в список подключенных игроков
        connectedPlayers.add(session);

        // Код, который выполняется при установке соединения с клиентом
        System.out.println("Player connected. Session ID: " + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Код для обработки входящих сообщений от клиента
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

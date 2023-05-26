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
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyWebSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LogManager.getLogger(PvPGameSession.class);
    private final PvPGameSessionMatcher pvPGameSessionMatcher;

    public MyWebSocketHandler(PvPGameSessionMatcher pvPGameSessionMatcher) {
        this.pvPGameSessionMatcher = pvPGameSessionMatcher;
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

        // Преобразование JSON-строки в объект JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Получение значения поля "eventType"
        String eventType = jsonNode.get("eventType").asText();

        if (eventType.equals("findPvPGameSession")) {
            PvPGameSession pvPGameSession =  pvPGameSessionMatcher.createPvPGameSession(playerSession);
            if (pvPGameSession != null) {
                log.info("pvPGameSession created");
                objectMapper = new ObjectMapper();
                try {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("eventType", "PvPGameSessionCreated");
                    objectNode.put("pvPGameModel", pvPGameSession.getPvPGameModel().serialize());
                    objectNode.put("serverSideSessionId", pvPGameSession.getPlayer1Session().getId());

                    String jsonMessage = objectMapper.writeValueAsString(objectNode);
                    pvPGameSession.getPlayer1Session().sendMessage(new TextMessage(jsonMessage));

                    objectNode.put("serverSideSessionId", pvPGameSession.getPlayer2Session().getId());
                    jsonMessage = objectMapper.writeValueAsString(objectNode);
                    pvPGameSession.getPlayer2Session().sendMessage(new TextMessage(jsonMessage));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }else {
                log.warn("pvPGameSession finding error");
            }
        }else if (eventType.equals("startGame")){
            String sessionId = jsonNode.get("sessionId").asText();
            PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

            if (pvPGameSession != null) {
                objectMapper = new ObjectMapper();
                try {
                    ObjectNode objectNode = objectMapper.createObjectNode();
                    objectNode.put("eventType", "gameStarted");
                    objectNode.put("pvPGameModel", pvPGameSession.getPvPGameModel().serialize());

                    String jsonMessage = objectMapper.writeValueAsString(objectNode);

                    pvPGameSession.getPlayer1Session().sendMessage(new TextMessage(jsonMessage));
                    pvPGameSession.getPlayer2Session().sendMessage(new TextMessage(jsonMessage));

                    ExecutorService executorService = Executors.newSingleThreadExecutor();
                    executorService.execute(pvPGameSession::startGame);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        }else if (eventType.equals("moveTetromino")){
            String sessionId = jsonNode.get("sessionId").asText();
            String direction = jsonNode.get("direction").asText();
            PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

            PvPGameModel pvPGameModel = pvPGameSession.getPvPGameModel();
            if (playerSession == pvPGameSession.getPlayer1Session()) {
                pvPGameSession.moveTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1(), Integer.parseInt(direction), playerSession);
            }else {
                pvPGameSession.moveTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2(), Integer.parseInt(direction), playerSession);
            }
        }else if (eventType.equals("rotateTetromino")) {
            String sessionId = jsonNode.get("sessionId").asText();
            PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

            PvPGameModel pvPGameModel = pvPGameSession.getPvPGameModel();
            if (playerSession == pvPGameSession.getPlayer1Session()) {
                pvPGameSession.rotateTetromino(pvPGameModel.getTetrominoPlayer1(), pvPGameModel.getGameMatrixPlayer1());
            }else {
                pvPGameSession.rotateTetromino(pvPGameModel.getTetrominoPlayer2(), pvPGameModel.getGameMatrixPlayer2());
            }
        }else if (eventType.equals("holdTetromino")) {
            String sessionId = jsonNode.get("sessionId").asText();
            PvPGameSession pvPGameSession = pvPGameSessionMatcher.getPvPGameSession(sessionId);

            pvPGameSession.holdTetromino(playerSession);
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

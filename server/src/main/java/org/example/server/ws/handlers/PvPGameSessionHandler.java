package org.example.server.ws.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.server.service.PvPGameSessionMatcher;
import org.springframework.web.socket.WebSocketSession;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public interface PvPGameSessionHandler {
    void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode);

    static Map<String, PvPGameSessionHandler> getMessageHandlersMap() throws URISyntaxException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        URL messageHandlersUrl = PvPGameSessionHandler.class.getResource("implementations/");
        assert messageHandlersUrl != null;
        File messageHandlers = new File(messageHandlersUrl.toURI());
        File[] messageHandlersFiles = messageHandlers.listFiles();

        Map<String, PvPGameSessionHandler> messageHandlersMap = new HashMap<>();

        assert messageHandlersFiles != null;
        for (File messageHandlerFile: messageHandlersFiles) {
            Class<?> clazz = Class.forName("org.example.server.ws.handlers.implementations." + messageHandlerFile.getName().replace(".class", ""));
            String handlerName = messageHandlerFile.getName().replace("Handler.class", "");
            messageHandlersMap.put(handlerName, (PvPGameSessionHandler) clazz.getDeclaredConstructor().newInstance());
        }

        return messageHandlersMap;
    }
}
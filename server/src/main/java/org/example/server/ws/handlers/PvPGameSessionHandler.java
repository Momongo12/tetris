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

/**
 * PvPGameSessionHandler is an interface that defines the contract for handling game session messages in a PvP (Player versus Player) game.
 * Implementations of this interface are responsible for processing incoming messages and performing the necessary actions based on the message content.
 * Each implementation should handle a specific type of message related to the PvP game session.
 * @version 1.0
 * @author Denis Moskvin
 */
public interface PvPGameSessionHandler {

    /**
     * Handles the incoming message related to the PvP game session.
     *
     * @param playerSession       The WebSocketSession of the player who sent the message.
     * @param pvPGameSessionMatcher The PvPGameSessionMatcher used to manage PvP game sessions.
     * @param jsonNode            The JSON node representing the content of the message.
     */
    void handle(WebSocketSession playerSession, PvPGameSessionMatcher pvPGameSessionMatcher, JsonNode jsonNode);

    /**
     * Retrieves a map of PvPGameSessionHandler implementations.
     *
     * @return A map of PvPGameSessionHandler implementations, where the keys are the names of the message handlers and the values are the handler instances.
     * @throws URISyntaxException       If there is an issue with the URI syntax.
     * @throws ClassNotFoundException If the handler implementation class cannot be found.
     * @throws NoSuchMethodException    If the handler implementation class does not have a default constructor.
     * @throws InvocationTargetException If an exception occurs while invoking the constructor of the handler implementation class.
     * @throws InstantiationException    If there is an issue with instantiating the handler implementation class.
     * @throws IllegalAccessException    If there is an illegal access issue while accessing the handler implementation class.
     */
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
package tetris.controller;

import tetris.model.GameLauncher;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient implements MessageHandler.Whole<String>{
    private GameLauncher gameLauncher;
    private Session session;

    public WebSocketClient(GameLauncher gameLauncher) {
        this.gameLauncher = gameLauncher;
    }

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("WebSocket connection established.");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
        // Обработка входящего сообщения
    }

    @OnClose
    public void onClose() {
        System.out.println("WebSocket connection closed.");
    }

    public void connect(String serverURI) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        try {
            container.connectToServer(this, URI.create(serverURI));
        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


package org.example.server.config;

import org.example.server.service.PvPGameSessionMatcher;
import org.example.server.service.PvPGameSessionMatcherImpl;
import org.example.server.ws.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * @version 1.0
 * @author Denis Moskvin
 */
@Configuration
@EnableWebSocket
public class WebSocketServer implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(pvPGameSessionMatcher()), "/game").setAllowedOrigins("*");
    }

    @Bean
    public PvPGameSessionMatcher pvPGameSessionMatcher(){
        return new PvPGameSessionMatcherImpl();
    }
}

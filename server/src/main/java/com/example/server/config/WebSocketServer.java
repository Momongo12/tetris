package com.example.server.config;

import com.example.server.service.PvPGameSessionMatcher;
import com.example.server.service.PvPGameSessionMatcherImpl;
import com.example.server.ws.MyWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

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

package com.connect.eduHub.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        // Only authenticated users can send and subscribe to certain topics
        messages
        .simpDestMatchers("/ws/**").permitAll()
            .simpDestMatchers("/app/**").authenticated()   // Requires authentication for /app/**
            .simpSubscribeDestMatchers("/user/**","/topic/**").authenticated() // Requires authentication for /topic/**
            .anyMessage().denyAll(); // Deny all other messages
    }

    // Optional: disable CSRF for WebSocket (if needed)
    @Override
    protected boolean sameOriginDisabled() {
        return true; // Disable same-origin policy for WebSocket if needed
    }
}

package com.movie.messagingservice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketBrokerConfig implements WebSocketMessageBrokerConfigurer {
    /************* ✨ Windsurf Command ⭐ *************/
    /**
     * Configures the message broker options.
     * Enables a simple in-memory message broker with a specified destination
     * prefix for topics. Sets the application destination prefix for messages
     * bound for methods annotated with @MessageMapping.
     *
     * @param config the message broker registry to configure
     */

    /******* 4130db45-4228-42b5-8fa1-5875f9b11a21 *******/
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/specific", "queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    /************* ✨ Windsurf Command ⭐ *************/
    /**
     * Registers a STOMP over WebSocket endpoint that clients can connect to, and
     * specifies the handshake handler to use. The handshake handler is used to
     * determine the Principal associated with the user performing the
     * WebSocket connection. The Principal is then used to authorize the user
     * to connect to any STOMP destinations. The endpoint is enabled for SockJS
     * fallback, which is useful for older browsers that don't support WebSockets.
     * Additionally, the endpoint is configured to send session cookies to the
     * client. This is necessary for the client to receive the session ID with
     * which to make subsequent requests to the server.
     */
    /******* cb3beedf-a9eb-4249-89b1-e927bd8fe796 *******/
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/messaging-ws")
                .setHandshakeHandler(new CustomHandshakeHandler())
                .setAllowedOrigins("*") // Only allow your frontend origin
                .withSockJS() // Enable SockJS fallback for older browsers
                .setSessionCookieNeeded(true); // Enable sending session cookies
    }
}
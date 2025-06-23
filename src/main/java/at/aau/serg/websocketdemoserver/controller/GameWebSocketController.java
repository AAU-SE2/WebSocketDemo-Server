package at.aau.serg.websocketdemoserver.controller;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.service.GameManager;
import at.aau.serg.websocketdemoserver.websocket.SessionUserRegistry;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Routes game‐related WebSocket messages to the correct GameHandler.
 */
@Controller
public class GameWebSocketController {

    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketController.class);

    private final SimpMessagingTemplate messagingTemplate;

    public GameWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Clients send to /app/dkt/{lobbyId}, subscribe to /topic/dkt/{lobbyId}.
     */
    @MessageMapping("/dkt/{lobbyId}")
    public void handleGameMessage(@DestinationVariable int lobbyId,
                                  @Payload GameMessage message,
                                  @Header("simpSessionId") String sessionId) {
        // Try to extract userId from payload if it's a RollDiceRequest
        String userId = null;
        if (message != null && message.getPayload() instanceof java.util.Map) {
            Object playerIdObj = ((java.util.Map<?, ?>) message.getPayload()).get("playerId");
            if (playerIdObj != null) {
                userId = String.valueOf(playerIdObj);
                SessionUserRegistry.register(sessionId, userId, lobbyId);
            }
        }

        if (lobbyId < 0) {
            logger.info("Ungültige Lobby-ID: " + lobbyId);
            return;
        }
        logger.info("Game {}: {} (session {} user {})", lobbyId, message.getType(), sessionId, userId);
        message.setLobbyId(lobbyId);
        var handler = GameManager.getInstance().getHandler(lobbyId);
        if (handler == null) {
            logger.info("Kein GameHandler für ID: " + lobbyId);
            return;
        }
        GameMessage result = handler.handle(message);
        logger.info("Sending result: {} to {}", message.getType(), lobbyId);
        if (result != null) {
            messagingTemplate.convertAndSend("/topic/dkt/" + lobbyId, result);
        }
        handler.getExtraMessages().forEach(extra ->
                messagingTemplate.convertAndSend("/topic/dkt/" + lobbyId, extra)
        );
    }
}

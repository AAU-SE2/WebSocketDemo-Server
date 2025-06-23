package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.dto.*;
import at.aau.serg.websocketdemoserver.service.lobby_request.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service handling lobby operations via dynamic LobbyRequest dispatcher.
 */
@Service
public class LobbyService {

    @Getter
    private final LobbyManager lobbyManager = new LobbyManager();
    private final UserManager userManager = new UserManager();
    private final Map<LobbyMessageType, LobbyRequest> requestHandlers = new HashMap<>();

    public LobbyService(SimpMessagingTemplate messagingTemplate) {
        // Alle LobbyRequests registrieren
        requestHandlers.put(LobbyMessageType.CREATE_USER, new CreateUserRequest(userManager));
        requestHandlers.put(LobbyMessageType.CREATE_LOBBY, new CreateLobbyRequest(lobbyManager));
        requestHandlers.put(LobbyMessageType.LIST_LOBBIES, new ListLobbiesRequest(lobbyManager));
        requestHandlers.put(LobbyMessageType.JOIN_LOBBY, new JoinLobbyRequest(lobbyManager, userManager));
        requestHandlers.put(LobbyMessageType.START_GAME, new StartGameRequest(lobbyManager, messagingTemplate));
    }

    public List<LobbyMessage> handle(LobbyMessage message) {
        if (message == null || message.getType() == null) {
            return List.of(new LobbyMessage(LobbyMessageType.ERROR, "Missing or invalid message."));
        }

        LobbyRequest handler = requestHandlers.get(message.getType());
        if (handler == null) {
            return List.of(new LobbyMessage(LobbyMessageType.ERROR, "No handler for message type: " + message.getType()));
        }

        try {
            return handler.handle(message);
        } catch (Exception e) {
            return List.of(new LobbyMessage(LobbyMessageType.ERROR, "Exception: " + e.getMessage()));
        }
    }
}

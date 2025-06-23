package at.aau.serg.websocketdemoserver.controller;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.dto.MessageType;
import at.aau.serg.websocketdemoserver.service.GameHandler;
import at.aau.serg.websocketdemoserver.service.GameManager;
import at.aau.serg.websocketdemoserver.service.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameWebSocketControllerTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private GameHandler mockHandler;

    @InjectMocks
    private GameWebSocketController controller;

    private  final int lobbyId = 42;
    String sessionId = "test-session-id";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        GameManager.getInstance().reset();
        GameManager.getInstance().getHandlerMapForTesting().put(lobbyId, mockHandler);
    }

    @Test
    void handleGameMessage_sendsResultAndExtraMessages() {
        // Arrange
        GameMessage input = new GameMessage();
        input.setType(MessageType.DICE_ROLLED);
        input.setPayload("{\"steps\": 6}");

        GameMessage result = new GameMessage();
        result.setType(MessageType.GAME_STATE);
        result.setPayload("{\"some\": \"data\"}");

        GameMessage extra1 = new GameMessage();
        extra1.setType(MessageType.CASH_TASK);
        extra1.setPayload("{\"amount\": 100}");

        when(mockHandler.handle(input)).thenReturn(result);
        when(mockHandler.getExtraMessages()).thenReturn(java.util.List.of(extra1));

        // Act
        controller.handleGameMessage(lobbyId, input, sessionId);

        // Assert
        verify(mockHandler).handle(input);
        verify(messagingTemplate).convertAndSend("/topic/dkt/" + lobbyId, result);
        verify(messagingTemplate).convertAndSend("/topic/dkt/" + lobbyId, extra1);
    }
}

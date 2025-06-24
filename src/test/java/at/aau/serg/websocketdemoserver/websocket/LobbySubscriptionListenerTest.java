package at.aau.serg.websocketdemoserver.websocket;

import at.aau.serg.websocketdemoserver.dto.LobbyMessage;
import at.aau.serg.websocketdemoserver.dto.LobbyMessageType;
import at.aau.serg.websocketdemoserver.dto.LobbyUpdatePayload;
import at.aau.serg.websocketdemoserver.dto.PlayerDTO;
import at.aau.serg.websocketdemoserver.service.Lobby;
import at.aau.serg.websocketdemoserver.service.LobbyManager;
import at.aau.serg.websocketdemoserver.service.LobbyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LobbySubscriptionListenerTest {

    @InjectMocks
    private LobbySubscriptionListener listener;

    @Mock
    private LobbyService lobbyService;

    @Mock
    private LobbyManager lobbyManager;
    @Mock
    private Lobby lobby;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private SessionSubscribeEvent event;

    @Mock
    private Message<byte[]> message;

    @BeforeEach
    void setup() {
        when(event.getMessage()).thenReturn(message);
    }

    @Test
    void shouldHandleListLobbiesAndSendLobbyUpdates() {
        // Arrange: STOMP headers
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination("/topic/lobby");
        accessor.setSessionId("session1");
        accessor.setSubscriptionId("sub1");

        MessageHeaders headers = accessor.getMessageHeaders();
        when(message.getHeaders()).thenReturn(headers);

        // Arrange: Mock response from LobbyService
        List<Map<String, Object>> lobbyList = new ArrayList<>();
        Map<String, Object> lobbyMap = new HashMap<>();
        lobbyMap.put("lobbyId", 1);
        lobbyList.add(lobbyMap);

        LobbyMessage listResponse = new LobbyMessage(LobbyMessageType.LOBBY_LIST, lobbyList);
        List<LobbyMessage> lobbyMessages = List.of(listResponse);

        when(lobbyService.handle(any())).thenReturn(lobbyMessages);

        int lobbyId = 1;
        when(lobbyService.getLobbyManager()).thenReturn(lobbyManager);
        when(lobbyManager.getLobby(lobbyId)).thenReturn(lobby);
        when(lobby.getPlayers()).thenReturn(List.of(new PlayerDTO(1, "player1"), new PlayerDTO(2, "player2")));

        // Act
        listener.onApplicationEvent(event);

        // Assert
        // LIST_LOBBIES was sent
        verify(messagingTemplate).convertAndSend(eq("/topic/lobby"), eq(listResponse));

        // LOBBY_UPDATE was sent
        ArgumentCaptor<LobbyMessage> updateCaptor = ArgumentCaptor.forClass(LobbyMessage.class);
        verify(messagingTemplate, times(2)).convertAndSend(eq("/topic/lobby"), updateCaptor.capture());

        LobbyMessage updateMessage = updateCaptor.getAllValues().get(1);
        assertEquals(LobbyMessageType.LOBBY_UPDATE, updateMessage.getType());
        assertEquals(1, updateMessage.getLobbyId());

        LobbyUpdatePayload payload = (LobbyUpdatePayload) updateMessage.getPayload();
        assertEquals(1, payload.getLobbyId());
        assertEquals("player1", payload.getPlayers().get(0).getNickname());
        assertEquals("player2", payload.getPlayers().get(1).getNickname());
    }

    @Test
    void shouldDoNothingIfDestinationIsNull() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        MessageHeaders headers = accessor.getMessageHeaders();
        when(message.getHeaders()).thenReturn(headers);

        listener.onApplicationEvent(event);

        verifyNoInteractions(messagingTemplate);
    }

    @Test
    void shouldNotSendAnythingOnOtherDestination() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setDestination("/topic/other");
        MessageHeaders headers = accessor.getMessageHeaders();
        when(message.getHeaders()).thenReturn(headers);

        listener.onApplicationEvent(event);

        verifyNoInteractions(messagingTemplate);
    }
}


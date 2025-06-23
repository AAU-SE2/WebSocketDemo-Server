package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.dto.MessageType;
import at.aau.serg.websocketdemoserver.model.gamestate.GameBoard;
import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessageFactoryTest {

    private GameState gameState;
    private Player player;

    @BeforeEach
    void setUp() {
        gameState = new GameState();
        player = new Player("Tester", gameState.getBoard());
        gameState.startGame(List.of(player));
    }

    @Test
    void testErrorMessage() {
        GameMessage msg = MessageFactory.error(1, "Fehler");
        assertEquals(1, msg.getLobbyId());
        assertEquals(MessageType.ERROR, msg.getType());
        assertEquals("Fehler", ((Map<?, ?>) msg.getPayload()).get("reason"));
    }

    @Test
    void testGameStateMessage() {
        GameMessage msg = MessageFactory.gameState(5, gameState);
        assertEquals(5, msg.getLobbyId());
        assertEquals(MessageType.GAME_STATE, msg.getType());

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
        assertTrue(payload.containsKey("currentPlayerId"));
        assertTrue(payload.containsKey("players"));
        assertTrue(payload.containsKey("currentRound"));
        assertTrue(payload.containsKey("board"));
    }

    @Test
    void testPlayerLost() {
        GameMessage msg = MessageFactory.playerLost(3, 42);
        assertEquals(3, msg.getLobbyId());
        assertEquals(MessageType.PLAYER_LOST, msg.getType());

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) msg.getPayload();
        assertEquals(42, payload.get("playerId"));
    }

    @Test
    void testGameOver() {
        Player testPlayer = new Player(1, "Tester", new GameBoard());

        GameMessage msg = MessageFactory.gameOver(7, testPlayer);

        assertEquals(7, msg.getLobbyId());
        assertEquals(MessageType.GAME_OVER, msg.getType());

        @SuppressWarnings("unchecked")
        Map<String, Object> payload = (Map<String, Object>) msg.getPayload();

        assertEquals(player.getId(), payload.get("winnerId"));
        assertEquals(player.getNickname(), payload.get("winnerName"));
    }
}

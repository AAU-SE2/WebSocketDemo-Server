package at.aau.serg.websocketdemoserver.service.game_request;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.dto.MessageType;
import at.aau.serg.websocketdemoserver.model.gamestate.GameBoard;
import at.aau.serg.websocketdemoserver.model.board.JailTile;
import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GoToJailRequestTest {

    private GameBoard board;
    private JailTile jailTile;
    private GameState gameState;
    private Player player;
    private GoToJailRequest request;
    private List<GameMessage> extraMessages;

    @BeforeEach
    void setup() {
        board = new GameBoard();
        jailTile = new JailTile(5);
        board.getTiles().set(5, jailTile);

        gameState = new GameState();
        player = new Player(1, "Alice", board);
        player.setCash(500);
        player.setCurrentTile(board.getTiles().get(0));
        gameState.startGame(List.of(player));

        extraMessages = new ArrayList<>();
        request = new GoToJailRequest(jailTile);
    }

    @Test
    void testPlayerHasEscapeCard() {
        player.setEscapeCard(true);
        var payload = new java.util.HashMap<String, Object>();
        payload.put("playerId", player.getId());
        request.execute(1, payload, gameState, extraMessages);
        assertNotEquals(jailTile, player.getCurrentTile());
        assertEquals(0, player.getSuspensionRounds());
    }

    @Test
    void testInvalidPlayer() {
        var payload = new java.util.HashMap<String, Object>();
        payload.put("playerId", 999); // Nicht existierend
        GameMessage result = request.execute(1, payload, gameState, extraMessages);
        assertEquals(MessageType.ERROR, result.getType());
    }

    @Test
    void testPlayerNotAlive() {
        player.setAlive(false);
        var payload = new java.util.HashMap<String, Object>();
        payload.put("playerId", player.getId());
        GameMessage result = request.execute(1, payload, gameState, extraMessages);
        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getPayload().toString().contains("ausgeschieden"));
    }

    @Test
    void testExceptionHandling() {
        GameMessage result = request.execute(1, "invalid", gameState, extraMessages);
        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getPayload().toString().contains("Fehler bei Gefängnisanweisung"));
    }
}

package at.aau.serg.websocketdemoserver.service.game_request;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.dto.MessageType;
import at.aau.serg.websocketdemoserver.model.board.*;
import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import at.aau.serg.websocketdemoserver.model.util.DicePair;
import at.aau.serg.websocketdemoserver.service.MessageFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RollDiceRequestTest {

    private GameState gameState;
    private Player mainPlayer;
    private Player otherPlayer;
    private DicePair dicePair;
    private RollDiceRequest request;
    private final int lobbyId = 1;

    @BeforeEach
    void setUp() {
        gameState = new GameState();

        mainPlayer = new Player(1, "Alice", gameState.getBoard());
        otherPlayer = new Player(2, "Bob", gameState.getBoard());

        gameState.getPlayersById().put(mainPlayer.getId(), mainPlayer);
        gameState.getPlayersById().put(otherPlayer.getId(), otherPlayer);
        gameState.setTurnOrder(List.of(mainPlayer, otherPlayer));
        gameState.setCurrentPlayerIndex(0);

        dicePair = mock(DicePair.class);
        when(dicePair.roll()).thenReturn(new int[]{1, 2});

        request = new RollDiceRequest(dicePair);
    }

    @Test
    void testInvalidPlayer() {
        Map<String, Object> payload = Map.of("playerId", 999);
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getPayload().toString().contains("Spieler nicht gefunden"));
    }

    @Test
    void testNotPlayersTurn() {
        Map<String, Object> payload = Map.of("playerId", otherPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getPayload().toString().contains("Nicht dein Zug"));
    }

    @Test
    void testAlreadyRolledDice() {
        mainPlayer.setHasRolledDice(true);
        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.ERROR, result.getType());
    }

    @Test
    void testGameOverOnlyOneAlive() {
        otherPlayer.eliminate();
        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.GAME_OVER, result.getType());
    }

    @Test
    void testSuspendedPlayerTriggersPrisonAskMessage() {
        mainPlayer.suspendForRounds(2);
        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.GAME_STATE, result.getType());
        assertEquals(1, extras.size());
        assertEquals(MessageType.ASK_PAY_PRISON, extras.get(0).getType());
    }

    @Test
    void testRollDiceMovesCorrectlyAndSendsState() {
        mainPlayer.moveToTile(1);
        when(dicePair.roll()).thenReturn(new int[]{1, 2}); // → pos 4

        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();
        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.GAME_STATE, result.getType());
        assertEquals(4, mainPlayer.getCurrentTile().getIndex());
    }

    @Test
    void testLandOnUnaffordableStreetTriggersMessage() {
        StreetTile street = new StreetTile(3, "Too Expensive", 1000, 50, StreetLevel.NORMAL, 100);
        gameState.getBoard().getTiles().set(2, street); // index 3
        mainPlayer.setCash(100);
        when(dicePair.roll()).thenReturn(new int[]{1, 1}); // → pos 3

        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();
        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.GAME_STATE, result.getType());
        assertEquals(3, mainPlayer.getCurrentTile().getIndex());
        assertEquals(MessageType.EXTRA_MESSAGE, extras.get(0).getType());
    }

    @Test
    void testLandOnOwnedStreetTransfersRent() {
        StreetTile street = new StreetTile(3, "Rent Street", 200, 50, StreetLevel.NORMAL, 100);
        street.setOwner(otherPlayer);
        mainPlayer.setCash(1000);
        otherPlayer.setCash(1000);
        gameState.getBoard().getTiles().set(2, street);
        when(dicePair.roll()).thenReturn(new int[]{1, 1}); // → pos 3

        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();
        request.execute(lobbyId, payload, gameState, extras);

        assertEquals(950, mainPlayer.getCash());
        assertEquals(1050, otherPlayer.getCash());
    }

    @Test
    void testLandOnBankTileTriggersBankCardDraw() {
        SpecialTile bank = new SpecialTile(5, "Bank", TileType.BANK);
        gameState.getBoard().getTiles().set(4, bank);
        mainPlayer.moveToTile(1);
        when(dicePair.roll()).thenReturn(new int[]{2, 2}); // → pos 5

        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();
        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.GAME_STATE, result.getType());
        assertEquals(5, mainPlayer.getCurrentTile().getIndex());
    }

    @Test
    void testRollDiceThrowsException() {
        when(dicePair.roll()).thenThrow(new RuntimeException("Test Error"));
        Map<String, Object> payload = Map.of("playerId", mainPlayer.getId());
        List<GameMessage> extras = new ArrayList<>();

        GameMessage result = request.execute(lobbyId, payload, gameState, extras);

        assertEquals(MessageType.ERROR, result.getType());
        assertTrue(result.getPayload().toString().contains("Fehler beim Würfeln"));
    }
}

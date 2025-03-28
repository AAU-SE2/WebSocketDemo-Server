package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;

import java.util.List;

public class GameHandlerTest {
    @Test
    void testHandleRollDiceReturnsPlayerMoved() {
        GameHandler handler = new GameHandler();
        String payload = "{\"playerId\": \"player1\"}";
        GameMessage input = new GameMessage("roll_dice", payload);

        GameMessage result = handler.handle(input);

        assertNotNull(result);
        assertEquals("player_moved", result.getType());
        assertTrue(result.getPayload().contains("player1"));
        assertTrue(result.getPayload().contains("pos"));
    }

    @Test
    void testPlayerMovedIncludesTileData() throws JSONException {
        GameHandler handler = new GameHandler();
        String payload = "{\"playerId\":\"player1\"}";

        GameMessage result = handler.handle(new GameMessage("roll_dice", payload));

        assertEquals("player_moved", result.getType());

        JSONObject obj = new JSONObject(result.getPayload());
        assertTrue(obj.has("tileName"), "tileName fehlt im Payload");
        assertTrue(obj.has("tileType"), "tileType fehlt im Payload");
    }

    @Test
    void testTileTypeIsValid() throws JSONException {
        GameHandler handler = new GameHandler();
        String payload = "{\"playerId\":\"test\"}";
        GameMessage result = handler.handle(new GameMessage("roll_dice", payload));

        JSONObject obj = new JSONObject(result.getPayload());
        String type = obj.getString("tileType");

        // Anpassen der erlaubten Typen
        assertTrue(
                type.matches("start|street|station|event|tax|jail|goto_jail|free"),
                "tileType ist kein gültiger Typ: " + type
        );
    }

    @Test
    void testPlayerPositionIsStored() {
        GameHandler handler = new GameHandler();
        String payload = "{\"playerId\":\"p1\"}";
        handler.handle(new GameMessage("roll_dice", payload));

        int pos = handler.getGameState().getPosition("p1");
        assertTrue(pos >= 0 && pos < 40, "Position liegt nicht im gültigen Bereich");
    }

    @Test
    void testMultipleRollsNoCrash() {
        GameHandler handler = new GameHandler();

        for (int i = 0; i < 10; i++) {
            String payload = "{\"playerId\":\"multi\"}";
            GameMessage result = handler.handle(new GameMessage("roll_dice", payload));
            assertEquals("player_moved", result.getType());
        }

        int pos = handler.getGameState().getPosition("multi");
        assertTrue(pos >= 0 && pos < 40);
    }

    @Test
    void testDecideActionForStreet() {
        GameHandler handler = new GameHandler();
        Tile tile = new Tile(1, "Museumsplatz", "street");

        GameMessage msg = handler.decideAction("player1", tile);
        assertEquals("can_buy_property", msg.getType());
    }

    @Test
    void testDecideActionForTax() {
        GameHandler handler = new GameHandler();
        Tile tile = new Tile(4, "Einkommenssteuer", "tax");

        GameMessage msg = handler.decideAction("player1", tile);
        assertEquals("pay_tax", msg.getType());
    }

    @Test
    void testDecideActionForEvent() {
        GameHandler handler = new GameHandler();
        Tile tile = new Tile(2, "Ereignisfeld", "event");

        GameMessage msg = handler.decideAction("player1", tile);
        assertEquals("draw_event_card", msg.getType());
    }

    @Test
    void testDecideActionForFreeField() {
        GameHandler handler = new GameHandler();
        Tile tile = new Tile(20, "Frei Parken", "free");

        GameMessage msg = handler.decideAction("player1", tile);
        assertEquals("skipped", msg.getType());
    }

    @Test
    void testExtraActionGeneratedAfterRoll() {
        GameHandler handler = new GameHandler();
        String payload = "{\"playerId\":\"player1\"}";
        handler.handle(new GameMessage("roll_dice", payload));

        List<GameMessage> extras = handler.getExtraMessages();
        assertEquals(1, extras.size(), "Es sollte genau eine Aktionsnachricht geben");

        GameMessage action = extras.get(0);
        assertNotNull(action.getType(), "Aktionstyp darf nicht null sein");
        assertTrue(action.getType().matches("can_buy_property|pay_tax|draw_event_card|go_to_jail|skipped"),
                "Unerwarteter Aktionstyp: " + action.getType());
    }

}

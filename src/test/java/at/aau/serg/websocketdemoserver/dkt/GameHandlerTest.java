package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.json.JSONObject;

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





}

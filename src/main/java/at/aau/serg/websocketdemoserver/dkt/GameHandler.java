package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONObject;
import java.util.Random;

public class GameHandler {

    private final GameState gameState = new GameState();

    public GameMessage handle(GameMessage msg) {
        switch (msg.getType()) {
            case "roll_dice":
                return handleRollDice(msg.getPayload());
            default:
                return new GameMessage("error", "Unbekannter Typ: " + msg.getType());
        }
    }

    private GameMessage handleRollDice(String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            String playerId = obj.getString("playerId");

            int dice = new Random().nextInt(6) + 1;
            int currentPos = gameState.getPosition(playerId);
            int newPos = (currentPos + dice) % 40;

            gameState.updatePosition(playerId, newPos);

            JSONObject result = new JSONObject();
            result.put("playerId", playerId);
            result.put("pos", newPos);
            result.put("dice", dice);

            System.out.println("Server: " + playerId + " moved to " + newPos + " (rolled " + dice + ")");
            return new GameMessage("player_moved", result.toString());

        } catch (Exception e) {
            return new GameMessage("error", "Fehler: " + e.getMessage());
        }
    }
}

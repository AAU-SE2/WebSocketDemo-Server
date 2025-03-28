package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONObject;
import java.util.Random;

public class GameHandler {

    private final GameState gameState = new GameState();
    private final GameBoard board = new GameBoard();

    public GameState getGameState() {
        return gameState;
    }

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
            Tile tile = board.getTileAt(newPos);


            gameState.updatePosition(playerId, newPos);

            JSONObject result = new JSONObject();
            result.put("playerId", playerId);
            result.put("pos", newPos);
            result.put("dice", dice);
            result.put("tileName", tile.getName());
            result.put("tileType", tile.getType());

            System.out.println("Server: " + playerId + " moved to " + newPos + " (rolled " + dice + ", field: " + tile.getName() + ")");

            return new GameMessage("player_moved", result.toString());

        } catch (Exception e) {
            return new GameMessage("error", "Fehler: " + e.getMessage());
        }
    }

    private GameMessage decideAction(String playerId, Tile tile) {
        JSONObject payload = new JSONObject();
        payload.put("playerId", playerId);
        payload.put("tilePos", tile.getPosition());
        payload.put("tileName", tile.getName());

        switch (tile.getType()) {
            case "street":
            case "station":
                return new GameMessage("can_buy_property", payload.toString());
            case "tax":
                return new GameMessage("pay_tax", payload.toString());
            case "event":
                return new GameMessage("draw_event_card", payload.toString());
            case "goto_jail":
                return new GameMessage("go_to_jail", payload.toString());
            default:
                return new GameMessage("noop", payload.toString()); // Keine Aktion nötig
        }
    }

}

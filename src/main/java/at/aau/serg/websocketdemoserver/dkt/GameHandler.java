package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameHandler {

    private final GameState gameState = new GameState();
    private final GameBoard board = new GameBoard();
    private final List<GameMessage> extraMessages = new ArrayList<>();

    public List<GameMessage> getExtraMessages() {
        return extraMessages;
    }


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
            gameState.updatePosition(playerId, newPos);

            Tile tile = board.getTileAt(newPos);

            JSONObject movePayload = new JSONObject();
            movePayload.put("playerId", playerId);
            movePayload.put("pos", newPos);
            movePayload.put("dice", dice);
            movePayload.put("tileName", tile.getName());
            movePayload.put("tileType", tile.getType());

            System.out.println("Server: " + playerId + " ist auf " + tile.getName() + " (" + tile.getType() + ")");

            // Aktion ermitteln (z.B.: kaufen, zahlen etc.)
            GameMessage actionMsg = decideAction(playerId, tile);
            System.out.println("→ Aktion: " + actionMsg.getType());

            // Nur player_moved senden (später: beide Nachrichten)
            return new GameMessage("player_moved", movePayload.toString());

        } catch (Exception e) {
            return new GameMessage("error", "Fehler: " + e.getMessage());
        }
    }


    GameMessage decideAction(String playerId, Tile tile) {
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
                return new GameMessage("skipped", payload.toString()); // Keine Aktion nötig
        }
    }

}

package at.aau.serg.websocketdemoserver.dkt;

import org.json.JSONObject;

import java.util.*;

public class GameHandler {

    private final GameState gameState = new GameState();
    private final GameBoard board = new GameBoard();
    private final List<GameMessage> extraMessages = new ArrayList<>();
    private final Map<Integer, String> ownership = new HashMap<>(); // Besitzverwaltung

    public List<GameMessage> getExtraMessages() {
        return extraMessages;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getOwner(int tilePos) {
        return ownership.get(tilePos);
    }

    public GameMessage handle(GameMessage msg) {
        switch (msg.getType()) {
            case "roll_dice":
                return handleRollDice(msg.getPayload());
            case "buy_property":
                return handleBuyProperty(msg.getPayload());
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

            // Aktion ermitteln
            GameMessage actionMsg = decideAction(playerId, tile);

            // Aktion zur Extraschlange hinzufügen
            extraMessages.clear();
            extraMessages.add(actionMsg);

            return new GameMessage("player_moved", movePayload.toString());

        } catch (Exception e) {
            return new GameMessage("error", "Fehler: " + e.getMessage());
        }
    }

    private GameMessage handleBuyProperty(String payload) {
        try {
            JSONObject obj = new JSONObject(payload);
            String playerId = obj.getString("playerId");
            int tilePos = obj.getInt("tilePos");

            if (ownership.containsKey(tilePos)) {
                return new GameMessage("error", "Feld gehört schon jemandem!");
            }

            ownership.put(tilePos, playerId);
            System.out.println("Besitz gespeichert: " + playerId + " → Feld " + tilePos);

            JSONObject response = new JSONObject();
            response.put("playerId", playerId);
            response.put("tilePos", tilePos);

            return new GameMessage("property_bought", response.toString());

        } catch (Exception e) {
            return new GameMessage("error", "Fehler beim Kauf: " + e.getMessage());
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
                String owner = ownership.get(tile.getPosition());
                if (owner != null && !owner.equals(playerId)) {
                    payload.put("ownerId", owner);
                    return new GameMessage("must_pay_rent", payload.toString());
                }
                return new GameMessage("can_buy_property", payload.toString());

            case "tax":
                return new GameMessage("pay_tax", payload.toString());

            case "event":
                return new GameMessage("draw_event_card", payload.toString());

            case "goto_jail":
                return new GameMessage("go_to_jail", payload.toString());

            default:
                return new GameMessage("skipped", payload.toString());
        }
    }
}
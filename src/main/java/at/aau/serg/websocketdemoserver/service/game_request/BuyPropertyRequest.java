package at.aau.serg.websocketdemoserver.service.game_request;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.model.board.StreetTile;
import at.aau.serg.websocketdemoserver.model.board.Tile;
import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import at.aau.serg.websocketdemoserver.service.GameRequest;
import at.aau.serg.websocketdemoserver.service.MessageFactory;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class BuyPropertyRequest implements GameRequest {

    @Override
    public GameMessage execute(int lobbyId, Object payload, GameState gameState, List<GameMessage> extraMessages) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) payload;
            JSONObject obj = new JSONObject(map);

            int playerId = obj.getInt("playerId");
            int tilePos = obj.getInt("tilePos");
            Player player = gameState.getPlayer(playerId);

            if (tilePos == -1) {
                gameState.advanceTurn();
                player.setHasRolledDice(false);
                return MessageFactory.gameState(lobbyId, gameState);
            }

            if (player == null || !player.isAlive()) {
                return MessageFactory.error(lobbyId, "Spieler ungültig oder bereits ausgeschieden.");
            }

            Tile tile = gameState.getBoard().getTile(tilePos);
            if (!(tile instanceof StreetTile street)) {
                return MessageFactory.error(lobbyId, "Feld ist nicht kaufbar.");
            }

            if (street.getOwner() != null) {
                return MessageFactory.error(lobbyId, "Straße bereits im Besitz.");
            }

            boolean success = player.purchaseStreet(tilePos);
            if (!success) {
                return MessageFactory.error(lobbyId, "Kauf fehlgeschlagen (z.B. nicht genug Geld).");
            }

            street.setOwner(player);

            // NEU: Markiere das Feld als frisch gekauft für diese Runde
            gameState.markTileBoughtThisTurn(tilePos);

            if (!player.isAlive()) {
                extraMessages.add(MessageFactory.playerLost(lobbyId, player.getId()));
            }

            gameState.advanceTurn();
            player.setHasRolledDice(false);
            return MessageFactory.gameState(lobbyId, gameState);

        } catch (Exception e) {
            e.printStackTrace();
            return MessageFactory.error(lobbyId, "Fehler beim Kaufen: " + e.getMessage());
        }
    }
}

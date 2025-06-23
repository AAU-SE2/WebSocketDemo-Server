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

public class BuildHouseRequest implements GameRequest {

    @Override
    public GameMessage execute(int lobbyId, Object payload, GameState gameState, List<GameMessage> extraMessages) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) payload;
            JSONObject obj = new JSONObject(map);

            int playerId = obj.getInt("playerId");
            int tilePos = obj.getInt("tilePos");

            Player player = gameState.getPlayer(playerId);
            if (player == null || !player.isAlive()) {
                return MessageFactory.error(lobbyId, "Spieler ungültig oder ausgeschieden.");
            }

            if (!gameState.isPlayersTurn(playerId)) {
                return MessageFactory.error(lobbyId, "Du bist nicht an der Reihe.");
            }

            // Check: bereits gebaut in dieser Runde?
            if (gameState.wasTileBuiltThisTurn(tilePos)) {
                return MessageFactory.error(lobbyId, "Auf diesem Feld wurde in dieser Runde bereits gebaut.");
            }

            // Check: Feld wurde in dieser Runde gekauft?
            if (gameState.wasTileBoughtThisTurn(tilePos)) {
                return MessageFactory.error(lobbyId, "Auf neu gekauften Feldern darf in derselben Runde nicht gebaut werden.");
            }

            Tile tile = gameState.getBoard().getTile(tilePos);
            if (!(tile instanceof StreetTile street)) {
                return MessageFactory.error(lobbyId, "Kein baubares Feld.");
            }

            if (!street.isOwner(player)) {
                return MessageFactory.error(lobbyId, "Du besitzt dieses Grundstück nicht.");
            }

            boolean success = street.buildHouse(player);
            if (!success) {
                return MessageFactory.error(lobbyId, "Hausbau nicht möglich.");
            }

            // Feld als „in dieser Runde bebaut“ markieren
            gameState.markTileBuiltThisTurn(tilePos);

            // Kein Rundenwechsel – Spieler darf noch andere Aktionen machen
            return MessageFactory.gameState(lobbyId, gameState);

        } catch (Exception e) {
            e.printStackTrace();
            return MessageFactory.error(lobbyId, "Fehler beim Hausbau: " + e.getMessage());
        }
    }
}

package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.dto.GameMessage;
import at.aau.serg.websocketdemoserver.dto.MessageType;
import at.aau.serg.websocketdemoserver.dto.PlayerDTO;
import at.aau.serg.websocketdemoserver.model.board.Tile;
import at.aau.serg.websocketdemoserver.model.cards.BankCardDeck;
import at.aau.serg.websocketdemoserver.model.cards.RiskCardDeck;
import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import at.aau.serg.websocketdemoserver.model.util.Dice;
import at.aau.serg.websocketdemoserver.model.util.DicePair;
import at.aau.serg.websocketdemoserver.service.game_request.*;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.*;

import static at.aau.serg.websocketdemoserver.dto.MessageType.*;

@Service
public class GameHandler {

    @Getter
    private final GameState gameState;
    private final Map<MessageType, GameRequest> requestMap = new HashMap<>();
    private final List<GameMessage> extraMessages = new ArrayList<>();

    public GameHandler(GameState gameState) {
        this.gameState = gameState;
        Tile jailTile = gameState.getBoard().getTile(31);

        // Mapping aller MessageTypes zu den zugehörigen Requests
        requestMap.put(ROLL_DICE, new RollDiceRequest(new DicePair(new Dice(1, 6), new Dice(1, 6))));
        requestMap.put(BUY_PROPERTY, new BuyPropertyRequest());
        requestMap.put(PAY_PRISON, new PayPrisonRequest());
        requestMap.put(ROLL_PRISON, new RollPrisonRequest(new DicePair(new Dice(1, 6), new Dice(1, 6))));
        requestMap.put(DRAW_BANK_CARD, new DrawBankCardRequest(BankCardDeck.get()));
        requestMap.put(DRAW_RISK_CARD, new DrawRiskCardRequest(RiskCardDeck.get()));
        requestMap.put(PAY_TAX, new PayTaxRequest());
        requestMap.put(GO_TO_JAIL, new GoToJailRequest(jailTile));
        requestMap.put(PAY_RENT, new PayRentRequest());
        requestMap.put(BUILD_HOUSE, new BuildHouseRequest());
        requestMap.put(BUILD_HOTEL, new BuildHotelRequest());
    }

    public GameMessage handle(GameMessage message) {
        extraMessages.clear();

        if (message == null) {
            // Kein Zugriff auf message.getLobbyId(), also -1 als Lobby zurückgeben
            return MessageFactory.error(-1, "Ungültige Nachricht.");
        }

        int lobbyId = message.getLobbyId();

        if (message.getType() == null) {
            return MessageFactory.error(lobbyId, "Ungültige Nachricht.");
        }

        if (message.getType() == REQUEST_GAME_STATE) {
            return MessageFactory.gameState(lobbyId, gameState);
        }

        GameRequest request = requestMap.get(message.getType());
        if (request == null) {
            return MessageFactory.error(lobbyId, "Unbekannter Nachrichtentyp: " + message.getType());
        }

        return request.execute(lobbyId, message.getPayload(), gameState, extraMessages);
    }

    public void initGame(List<PlayerDTO> players) {
        List<Player> playerModels = players.stream()
                .map(dto -> new Player(dto.getId(), dto.getNickname(), gameState.getBoard()))
                .toList();

        gameState.startGame(playerModels);
    }

    public void removePlayer(int userId) {
        Player player = gameState.getPlayer(userId);
        if (player != null) {
            player.setAlive(false);
        }
    }

    public List<GameMessage> getExtraMessages() {
        return new ArrayList<>(extraMessages);
    }

    public String getCurrentPlayerId() {
        Player current = gameState.getCurrentPlayer();
        return current != null ? String.valueOf(current.getId()) : null;
    }
}

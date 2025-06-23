package at.aau.serg.websocketdemoserver.service.lobby_request;

import at.aau.serg.websocketdemoserver.dto.*;
import at.aau.serg.websocketdemoserver.model.gamestate.*;
import at.aau.serg.websocketdemoserver.service.*;
import at.aau.serg.websocketdemoserver.service.helper.LobbyHelper;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StartGameRequest implements LobbyRequest {

    private final LobbyManager lobbyManager;
    private final SimpMessagingTemplate messagingTemplate;

    public StartGameRequest(LobbyManager lobbyManager, SimpMessagingTemplate messagingTemplate) {
        this.lobbyManager = lobbyManager;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public List<LobbyMessage> handle(LobbyMessage message) {
        int lobbyId = message.getLobbyId();
        Lobby lobby = lobbyManager.getLobby(lobbyId);

        if (lobby == null || lobby.getPlayers().size() < 2) {
            return List.of(new LobbyMessage(
                    LobbyMessageType.ERROR, "Nicht genügend Spieler!"
            ));
        }

        // Spieler-Daten in GamePlayer konvertieren
        GameBoard board = new GameBoard();
        List<Player> playerModels = lobby.getPlayers().stream()
                .map(dto -> new Player(dto.getId(), dto.getNickname(), board))
                .collect(Collectors.toList());

        // GameState initialisieren
        GameState gameState = new GameState();
        gameState.startGame(playerModels);

        // Registrierung
        GameManager.getInstance().registerGame(lobbyId, gameState);

        // Reihenfolge festlegen und als DTO senden
        List<PlayerDTO> orderedDtos = gameState.getAllPlayers().stream()
                .map(p -> new PlayerDTO(p.getId(), p.getNickname()))
                .collect(Collectors.toList());

        GameStartPayload payload = new GameStartPayload(orderedDtos);
        LobbyMessage response = new LobbyMessage(lobbyId, LobbyMessageType.START_GAME, payload);

        // remove lobby after game start
        lobbyManager.removeLobby(lobbyId);
        // send lobby list update new LobbyMessage(LobbyMessageType.LOBBY_LIST, lobbyList)
        LobbyMessage lobbyListMessage = new LobbyMessage(LobbyMessageType.LOBBY_LIST, LobbyHelper.getLobbyList(lobbyManager));

        // ✉️ Nachrichten an alle Spieler
        sendGameStartedOrder(lobbyId, payload);
        sendCurrentPlayer(lobbyId, gameState.getCurrentPlayerId());

        return List.of(response, lobbyListMessage);
    }

    private void sendCurrentPlayer(int lobbyId, int playerId) {
        GameMessage msg = new GameMessage(
                lobbyId,
                MessageType.CURRENT_PLAYER,
                new CurrentPlayerPayload(playerId)
        );
        messagingTemplate.convertAndSend("/topic/dkt/" + lobbyId, msg);
    }

    private void sendGameStartedOrder(int lobbyId, GameStartPayload payload) {
        GameMessage msg = new GameMessage(
                lobbyId,
                MessageType.GAME_STARTED,
                payload
        );
        messagingTemplate.convertAndSend("/topic/dkt/" + lobbyId, msg);
    }
}
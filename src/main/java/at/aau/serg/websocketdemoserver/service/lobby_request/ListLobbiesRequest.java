package at.aau.serg.websocketdemoserver.service.lobby_request;

import at.aau.serg.websocketdemoserver.dto.LobbyMessage;
import at.aau.serg.websocketdemoserver.dto.LobbyMessageType;
import at.aau.serg.websocketdemoserver.service.LobbyManager;
import at.aau.serg.websocketdemoserver.service.LobbyRequest;
import at.aau.serg.websocketdemoserver.service.helper.LobbyHelper;

import java.util.List;
import java.util.Map;

public class ListLobbiesRequest implements LobbyRequest {

    private final LobbyManager lobbyManager;

    public ListLobbiesRequest(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    @Override
    public List<LobbyMessage> handle(LobbyMessage message) {
        List<Map<String, Object>> lobbyList =  LobbyHelper.getLobbyList(lobbyManager);

        return List.of(new LobbyMessage(LobbyMessageType.LOBBY_LIST, lobbyList));
    }
}

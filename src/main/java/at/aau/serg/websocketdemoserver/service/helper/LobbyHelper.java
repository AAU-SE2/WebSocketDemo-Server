package at.aau.serg.websocketdemoserver.service.helper;

import at.aau.serg.websocketdemoserver.service.Lobby;
import at.aau.serg.websocketdemoserver.service.LobbyManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LobbyHelper {

    private LobbyHelper() {
    }

    public static List<Map<String, Object>> getLobbyList(LobbyManager lobbyManager) {
        return lobbyManager.getLobbyIds().stream()
                .map(id -> {
                    Lobby l = lobbyManager.getLobby(id);
                    Map<String, Object> entry = new HashMap<>();
                    entry.put("lobbyId", id);
                    entry.put("lobbyName", l != null ? l.getLobbyName() : "Unknown");
                    entry.put("playerCount", l != null ? l.getPlayers().size() : 0);
                    return entry;
                }).toList();
    }

}

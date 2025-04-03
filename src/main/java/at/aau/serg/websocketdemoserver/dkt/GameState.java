package at.aau.serg.websocketdemoserver.dkt;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private final Map<String, Integer> playerPositions = new HashMap<>();

    public int getPosition(String playerId) {
        return playerPositions.getOrDefault(playerId, 0);
    }

    public void updatePosition(String playerId, int newPos) {
        playerPositions.put(playerId, newPos);
    }
}

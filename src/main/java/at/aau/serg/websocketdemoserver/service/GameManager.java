package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.model.gamestate.GameState;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class GameManager {
    @Getter
    private static final GameManager instance = new GameManager();
    @Getter
    private final Map<Integer, GameHandler> handlers = new HashMap<>();

    private GameManager() {}

    public void registerGame(int lobbyId, GameState gameState) {
        GameHandler handler = new GameHandler(gameState);  // neuer Konstruktor nötig
        handlers.put(lobbyId, handler);
    }

    public GameHandler getHandler(int lobbyId) {
        return handlers.get(lobbyId);
    }

    public void removeGame(int lobbyId) {
        handlers.remove(lobbyId);
    }
    public void reset() {
        handlers.clear();
    }

    // Nur für Tests
    public Map<Integer, GameHandler> getHandlerMapForTesting() {
        return handlers;
    }
}
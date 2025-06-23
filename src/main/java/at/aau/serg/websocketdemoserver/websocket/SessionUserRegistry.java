package at.aau.serg.websocketdemoserver.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class SessionUserRegistry {
    private static final ConcurrentHashMap<String, String> sessionIdToUserId = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> sessionIdToGameId = new ConcurrentHashMap<>();

    public static void register(String sessionId, String userId, Integer gameId) {
        if (sessionId != null && userId != null && gameId != null) {
            sessionIdToUserId.put(sessionId, userId);
            sessionIdToGameId.put(sessionId, gameId);
        }
    }

    public static Integer getUserId(String sessionId) {
        return sessionIdToUserId.get(sessionId) != null ? Integer.parseInt(sessionIdToUserId.get(sessionId)) : null;
    }

    public static Integer getGameId(String sessionId) {
        return sessionIdToGameId.get(sessionId);
    }

    public static void unregister(String sessionId) {
        sessionIdToUserId.remove(sessionId);
        sessionIdToGameId.remove(sessionId);
    }
}

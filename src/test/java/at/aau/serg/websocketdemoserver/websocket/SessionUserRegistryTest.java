package at.aau.serg.websocketdemoserver.websocket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SessionUserRegistryTest {

    private final String sessionId = "session-123";
    private final String userId = "42";
    private final Integer gameId = 99;

    @AfterEach
    void cleanup() {
        // Stelle sicher, dass die Registry nach jedem Test leer ist
        SessionUserRegistry.unregister(sessionId);
    }

    @Test
    void testRegisterAndGetUserId() {
        SessionUserRegistry.register(sessionId, userId, gameId);
        Integer retrievedUserId = SessionUserRegistry.getUserId(sessionId);
        assertNotNull(retrievedUserId);
        assertEquals(Integer.valueOf(userId), retrievedUserId);
    }

    @Test
    void testRegisterAndGetGameId() {
        SessionUserRegistry.register(sessionId, userId, gameId);
        Integer retrievedGameId = SessionUserRegistry.getGameId(sessionId);
        assertNotNull(retrievedGameId);
        assertEquals(gameId, retrievedGameId);
    }

    @Test
    void testUnregisterRemovesMappings() {
        SessionUserRegistry.register(sessionId, userId, gameId);
        SessionUserRegistry.unregister(sessionId);

        assertNull(SessionUserRegistry.getUserId(sessionId));
        assertNull(SessionUserRegistry.getGameId(sessionId));
    }

    @Test
    void testRegisterNullValuesDoesNothing() {
        SessionUserRegistry.register(null, userId, gameId);
        SessionUserRegistry.register(sessionId, null, gameId);
        SessionUserRegistry.register(sessionId, userId, null);

        assertNull(SessionUserRegistry.getUserId(sessionId));
        assertNull(SessionUserRegistry.getGameId(sessionId));
    }
}


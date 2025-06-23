package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.dto.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserManagerTest {

    private UserManager userManager;

    @BeforeEach
    void setUp() {
        userManager = new UserManager();
    }

    @Test
    void testCreateUser() {
        PlayerDTO player = userManager.createUser("Alice");
        assertNotNull(player);
        assertEquals(1, player.getId());
        assertEquals("Alice", player.getNickname());

        PlayerDTO player2 = userManager.createUser("Bob");
        assertEquals(2, player2.getId());
        assertEquals("Bob", player2.getNickname());
    }

    @Test
    void testGetPlayers() {
        userManager.createUser("Alice");
        userManager.createUser("Bob");

        List<PlayerDTO> players = userManager.getPlayers();
        assertEquals(2, players.size());
        assertEquals("Alice", players.get(0).getNickname());
        assertEquals("Bob", players.get(1).getNickname());

        PlayerDTO dto = new PlayerDTO(3, "Charlie");
        assertThrows(UnsupportedOperationException.class, () ->
                players.add(dto)
        );
    }

    @Test
    void testGetPlayerById() {
        PlayerDTO p1 = userManager.createUser("Alice");
        PlayerDTO p2 = userManager.createUser("Bob");

        PlayerDTO result1 = userManager.getPlayer(1);
        PlayerDTO result2 = userManager.getPlayer(2);
        PlayerDTO result3 = userManager.getPlayer(999); // nicht vorhanden

        assertEquals(p1, result1);
        assertEquals(p2, result2);
        assertNull(result3);
    }
}

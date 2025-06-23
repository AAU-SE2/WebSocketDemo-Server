package at.aau.serg.websocketdemoserver.service;

import at.aau.serg.websocketdemoserver.dto.PlayerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LobbyTest {

    private Lobby lobby;

    @BeforeEach
    void setUp() {
        lobby = new Lobby("TestLobby");
    }

    @Test
    void testGetLobbyName() {
        assertEquals("TestLobby", lobby.getLobbyName());
    }

    @Test
    void testAddPlayerAndGetPlayers() {
        PlayerDTO player = new PlayerDTO(1, "Alice");
        PlayerDTO result = lobby.addPlayer(player);

        assertEquals(player, result);
        List<PlayerDTO> players = lobby.getPlayers();
        assertEquals(1, players.size());
        assertEquals("Alice", players.get(0).getNickname());
    }

    @Test
    void testPlayersListIsUnmodifiable() {
        PlayerDTO player = new PlayerDTO(2, "Bob");
        lobby.addPlayer(player);
        List<PlayerDTO> players = lobby.getPlayers();

        PlayerDTO newPlayer = new PlayerDTO(3, "Eve");
        assertThrows(UnsupportedOperationException.class, () ->
                players.add(newPlayer)
        );
    }

    @Test
    void testIsReadyToStartWithEnoughPlayers() {
        lobby.addPlayer(new PlayerDTO(1, "Alice"));
        lobby.addPlayer(new PlayerDTO(2, "Bob"));
        assertTrue(lobby.isReadyToStart());
    }

    @Test
    void testIsReadyToStartWithInsufficientPlayers() {
        lobby.addPlayer(new PlayerDTO(1, "Alice"));
        assertFalse(lobby.isReadyToStart());
    }

    @Test
    void testClearRemovesAllPlayers() {
        lobby.addPlayer(new PlayerDTO(1, "Alice"));
        lobby.addPlayer(new PlayerDTO(2, "Bob"));
        lobby.clear();

        assertTrue(lobby.getPlayers().isEmpty());
        assertFalse(lobby.isReadyToStart());
    }
}

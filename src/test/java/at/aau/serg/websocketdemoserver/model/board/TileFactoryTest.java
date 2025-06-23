package at.aau.serg.websocketdemoserver.model.board;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TileFactoryTest {
    @Test
    void testCreateTiles() {
        List<Tile> tiles = TileFactory.createTiles();
        assertFalse(tiles.isEmpty());
        assertTrue(tiles.stream().anyMatch(t -> t.getIndex() == 1 && t.getType() == TileType.START));
        assertTrue(tiles.stream().anyMatch(StreetTile.class::isInstance));
    }
}

package at.aau.serg.websocketdemoserver.model.board;

import lombok.Setter;

public abstract class Tile {

    private final int index;
    @Setter
    private String label = "";

    protected Tile(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public abstract TileType getType();
}
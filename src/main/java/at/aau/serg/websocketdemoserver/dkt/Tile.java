package at.aau.serg.websocketdemoserver.dkt;

public class Tile {
    private final int position;
    private final String name;
    private final String type;

    public Tile(int position, String name, String type) {
        this.position = position;
        this.name = name;
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}

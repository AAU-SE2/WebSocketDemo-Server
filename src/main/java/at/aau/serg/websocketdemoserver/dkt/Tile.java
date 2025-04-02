package at.aau.serg.websocketdemoserver.dkt;

public class Tile {
    private final int position;
    private final String name;


    public Tile(int position, String name) {
        this.position = position;
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public String getTileType() {
        return this.getClass().getSimpleName();
    }

}

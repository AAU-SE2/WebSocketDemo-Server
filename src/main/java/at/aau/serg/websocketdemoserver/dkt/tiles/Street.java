package at.aau.serg.websocketdemoserver.dkt.tiles;

import at.aau.serg.websocketdemoserver.dkt.Tile;

public class Street extends Tile {

    private final int price;
    private final int rent;
    private final int houseCost;


    public Street(int position, String name, int price, int rent, int houseCost) {
        super(position, name);
        this.price = price;
        this.rent = rent;
        this.houseCost = houseCost;

    }

    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public int getHouseCost() {
        return houseCost;
    }
}

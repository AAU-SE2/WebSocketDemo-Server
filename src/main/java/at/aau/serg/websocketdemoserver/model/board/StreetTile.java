package at.aau.serg.websocketdemoserver.model.board;

import at.aau.serg.websocketdemoserver.model.gamestate.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StreetTile extends Tile {

    @Getter
    private final int price;
    @Getter
    private final int baseRent;
    @Getter
    private final StreetLevel level;
    @Getter
    private final int houseCost;
    @Getter
    private final int hotelCost;

    private final List<BuildingType> buildings = new ArrayList<>();
    @Getter
    private Player owner;

    public StreetTile(int index, String label, int price, int baseRent, StreetLevel level, int houseCost) {
        this(index, label, price, baseRent, level, houseCost, houseCost * 2);
    }

    public StreetTile(int index, String label, int price, int baseRent, StreetLevel level, int houseCost, int hotelCost) {
        super(index);
        setLabel(label);
        this.price = price;
        this.baseRent = baseRent;
        this.level = level;
        this.houseCost = houseCost;
        this.hotelCost = hotelCost;
    }

    public void setOwner(Player player) {
        if (player == null) {
            this.owner = null;
            clearBuildings();
        } else {
            this.owner = player;
        }
    }

    // --------------------------------------------
    // Mieten / Wert
    // --------------------------------------------

    public int calculateRent() {
        if (buildings.isEmpty()) return baseRent;

        double factor = switch (level) {
            case CHEAP -> 0.5;
            case NORMAL -> 1.0;
            case PREMIUM -> 1.5;
        };

        if (buildings.contains(BuildingType.HOTEL)) {
            return (int) (baseRent + baseRent * factor * 6);
        }

        return (int) (baseRent + baseRent * buildings.size() * factor);
    }

    public int calculateRawValue() {
        return price + getHouseCount() * houseCost + getHotelCount() * hotelCost;
    }

    public int calculateSellValue() {
        double value = price * 0.5;
        value += getHouseCount() * houseCost * 0.25;
        value += getHotelCount() * hotelCost * 0.25;
        return (int) value;
    }

    // --------------------------------------------
    // Haus / Hotel-Logik
    // --------------------------------------------

    public boolean buildHouse(Player player) {
        if (!isOwner(player)) return false;
        if (buildings.contains(BuildingType.HOTEL)) return false;
        if (getHouseCount() >= 4) return false;
        if (player.getCash() < houseCost) return false;

        buildings.add(BuildingType.HOUSE);
        player.setCash(player.getCash() - houseCost);
        return true;
    }

    public boolean buildHotel(Player player) {
        if (!isOwner(player)) return false;
        if (buildings.contains(BuildingType.HOTEL)) return false;
        if (getHouseCount() < 4) return false;
        if (player.getCash() < hotelCost) return false;

        buildings.clear();
        buildings.add(BuildingType.HOTEL);
        player.setCash(player.getCash() - hotelCost);
        return true;
    }

    // --------------------------------------------
    // Zugriff und Utility
    // --------------------------------------------

    public void clearBuildings() {
        buildings.clear();
    }

    public int getHouseCount() {
        return Collections.frequency(buildings, BuildingType.HOUSE);
    }

    public int getHotelCount() {
        return Collections.frequency(buildings, BuildingType.HOTEL);
    }

    public boolean isOwner(Player player) {
        return owner != null && owner.getId() == player.getId();
    }

    public int getOwnerId() {
        return (owner != null) ? owner.getId() : -1;
    }

    public String getOwnerName() {
        return (owner != null) ? owner.getNickname() : "BANK";
    }

    // --------------------------------------------
    // Meta-Infos
    // --------------------------------------------

    public List<BuildingType> getBuildings() {
        return new ArrayList<>(buildings);
    }

    public String getName() {
        return getLabel(); // Label kommt aus Tile
    }

    @Override
    public TileType getType() {
        return TileType.STREET;
    }
}

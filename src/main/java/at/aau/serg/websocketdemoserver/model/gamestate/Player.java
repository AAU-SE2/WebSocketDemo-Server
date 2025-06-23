package at.aau.serg.websocketdemoserver.model.gamestate;

import at.aau.serg.websocketdemoserver.model.board.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
// tile factory import

@Getter
@Setter
public class Player implements Comparable<Player> {

    private static int idCounter = 1;

    private final int id;
    private String nickname;
    private Tile currentTile;
    private int cash;
    private boolean alive;
    private final List<StreetTile> ownedStreets = new ArrayList<>();
    private int suspensionRounds;
    private boolean hasEscapeCard;
    private boolean hasRolledDice = false;
    private final GameBoard board;

    public Player(String nickname, GameBoard board) {
        this.id = idCounter++;
        this.currentTile = new SpecialTile(1, "START", TileType.START);
        this.nickname = nickname;
        this.cash = 3000;
        this.alive = true;
        this.board = board;
    }

    public Player(int id, String nickname, GameBoard board) {
        this.id = id;
        this.currentTile = new SpecialTile(1, "START", TileType.START);
        this.nickname = nickname;
        this.cash = 3000;
        this.alive = true;
        this.board = board;
    }

    public void setCash(int newCash) {
        this.cash = newCash;
        if (this.cash < 0) {
            eliminate();
        }
    }

    public void addCash(int amount) {
        setCash(this.cash + amount);
    }

    public void deductCash(int amount) {
        setCash(this.cash - amount);
    }
    public boolean adjustCash(int delta) {
        this.cash += delta;
        if (this.cash < 0) {
            eliminate();
            return true;
        }
        return false;
    }

    public void eliminate() {
        this.alive = false;
        this.cash = 0;
        // alle Streets freigeben
        for (StreetTile street : ownedStreets) {
            street.setOwner(null);
            street.clearBuildings();
        }
        ownedStreets.clear();
    }

    public boolean hasEscapeCard() {
        return hasEscapeCard;
    }

    public void setEscapeCard(boolean hasEscapeCard) {
        this.hasEscapeCard = hasEscapeCard;
    }

    public boolean isSuspended() {
        return suspensionRounds > 0;
    }

    public void decreaseSuspension() {
        if (suspensionRounds > 0) {
            suspensionRounds--;
        }
    }

    public void suspendForRounds(int rounds) {
        this.suspensionRounds = rounds;
    }

    public List<StreetTile> getOwnedStreets() {
        return new ArrayList<>(ownedStreets);
    }

    public boolean purchaseStreet(int position) {
        Tile tile = board.getTile(position);
        if (!(tile instanceof StreetTile street)) return false;
        if (street.getOwner() != null || street.getPrice() > cash) return false;

        deductCash(street.getPrice());
        street.setOwner(this);
        ownedStreets.add(street);
        return true;
    }

    public boolean sellStreet(int position) {
        Tile tile = board.getTile(position);
        if (!(tile instanceof StreetTile street)) return false;
        if (!ownedStreets.contains(street)) return false;

        addCash(street.calculateSellValue());
        street.setOwner(null);
        street.clearBuildings();
        ownedStreets.remove(street);
        return true;
    }

    public void moveToTile(int index) {
        if (!isSuspended()) {
            Tile destination = board.getTile(index);
            this.currentTile = destination;
        }
    }

    public void moveSteps(int steps) {
        System.out.println("Moving " + steps + " steps from " + currentTile);
        if (!isSuspended()) {
            int currentIndex = (currentTile != null)
                    ? currentTile.getIndex()
                    : 1;            // or whatever your “start” tile is

            int totalTiles   = board.getTiles().size();  // 40
            // step 1: shift into 0-based by subtracting 1
            int zeroBased    = currentIndex - 1;
            // step 2: add steps and wrap around with % totalTiles
            int wrapped      = (zeroBased + steps) % totalTiles;
            // step 3: shift back into 1-based by adding 1
            int newIndex     = wrapped + 1;

            moveToTile(newIndex);
        }
    }

    public int calculateWealth() {
        int total = cash;
        for (StreetTile street : ownedStreets) {
            total += street.getPrice();
            total += street.getHouseCost() * street.getBuildings().stream().filter(b -> b == BuildingType.HOUSE).count();
            total += street.getHotelCost() * street.getBuildings().stream().filter(b -> b == BuildingType.HOTEL).count();
        }
        return total;
    }

    public void transferCash(Player receiver, int amount) {
        this.deductCash(amount);
        receiver.addCash(amount);
    }

    public static void resetIdCounter() {
        idCounter = 1;
    }

    @Override
    public int compareTo(Player other) {
        return Integer.compare(this.id, other.id);
    }
}

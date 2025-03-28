package at.aau.serg.websocketdemoserver.dkt;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    private final List<Tile> tiles = new ArrayList<>();

    public GameBoard() {
        tiles.add(new Tile(0, "Los", "start"));
        tiles.add(new Tile(1, "Kärntner Straße", "street"));
        tiles.add(new Tile(2, "Ereignisfeld", "event"));
        tiles.add(new Tile(3, "Bahnhof West", "station"));
        tiles.add(new Tile(4, "Einkommenssteuer", "tax"));
        tiles.add(new Tile(5, "Opernring", "street"));
        tiles.add(new Tile(6, "Ereignisfeld", "event"));
        tiles.add(new Tile(7, "Bahnhof Süd", "station"));
        tiles.add(new Tile(8, "Mariahilfer Straße", "street"));
        tiles.add(new Tile(9, "Strafsteuer", "tax"));
        tiles.add(new Tile(10, "Gefängnis", "jail"));
        tiles.add(new Tile(11, "Schönbrunner Straße", "street"));
        tiles.add(new Tile(12, "Ereignisfeld", "event"));
        tiles.add(new Tile(13, "Bahnhof Ost", "station"));
        tiles.add(new Tile(14, "Technologieabgabe", "tax"));
        tiles.add(new Tile(15, "Landstraßer Hauptstraße", "street"));
        tiles.add(new Tile(16, "Ereignisfeld", "event"));
        tiles.add(new Tile(17, "Bahnhof Nord", "station"));
        tiles.add(new Tile(18, "Favoritenstraße", "street"));
        tiles.add(new Tile(19, "Einkommenssteuer", "tax"));
        tiles.add(new Tile(20, "Frei Parken", "free"));
        tiles.add(new Tile(21, "Praterstraße", "street"));
        tiles.add(new Tile(22, "Ereignisfeld", "event"));
        tiles.add(new Tile(23, "Gürtel", "street"));
        tiles.add(new Tile(24, "Bahnhof Mitte", "station"));
        tiles.add(new Tile(25, "Ringstraße", "street"));
        tiles.add(new Tile(26, "Ereignisfeld", "event"));
        tiles.add(new Tile(27, "Donauuferstraße", "street"));
        tiles.add(new Tile(28, "Luxussteuer", "tax"));
        tiles.add(new Tile(29, "Untere Donaustraße", "street"));
        tiles.add(new Tile(30, "Gehe ins Gefängnis", "goto_jail"));
        tiles.add(new Tile(31, "Lassallestraße", "street"));
        tiles.add(new Tile(32, "Ereignisfeld", "event"));
        tiles.add(new Tile(33, "Währinger Straße", "street"));
        tiles.add(new Tile(34, "Bahnhof Flughafen", "station"));
        tiles.add(new Tile(35, "Billrothstraße", "street"));
        tiles.add(new Tile(36, "Ereignisfeld", "event"));
        tiles.add(new Tile(37, "Heiligenstädter Straße", "street"));
        tiles.add(new Tile(38, "Strafsteuer", "tax"));
        tiles.add(new Tile(39, "Stephansplatz", "street"));
    }

    public Tile getTileAt(int position) {
        return tiles.get(position % tiles.size());
    }
}

package at.aau.serg.websocketdemoserver.dkt;

import java.util.ArrayList;
import java.util.List;

import at.aau.serg.websocketdemoserver.dkt.tiles.*;

public class GameBoard {
    private final List<Tile> tiles = new ArrayList<>();

    public GameBoard() {
        tiles.add(new Start(0, "Los"));
        tiles.add(new Street(1, "Kärntner Straße", 200, 50, 100));
        tiles.add(new Event(2, "Ereignisfeld"));
        tiles.add(new Station(3, "Bahnhof West"));
        tiles.add(new Tax(4, "Einkommenssteuer", 100));
        tiles.add(new Street(5, "Opernring", 220, 55, 110));
        tiles.add(new Event(6, "Ereignisfeld"));
        tiles.add(new Station(7, "Bahnhof Süd"));
        tiles.add(new Street(8, "Mariahilfer Straße", 240, 60, 120));
        tiles.add(new Tax(9, "Strafsteuer", 150));
        tiles.add(new Jail(10, "Gefängnis"));
        tiles.add(new Street(11, "Schönbrunner Straße", 260, 65, 130));
        tiles.add(new Event(12, "Ereignisfeld"));
        tiles.add(new Station(13, "Bahnhof Ost"));
        tiles.add(new Tax(14, "Technologieabgabe", 200));
        tiles.add(new Street(15, "Landstraßer Hauptstraße", 280, 70, 140));
        tiles.add(new Event(16, "Ereignisfeld"));
        tiles.add(new Station(17, "Bahnhof Nord"));
        tiles.add(new Street(18, "Favoritenstraße", 300, 75, 150));
        tiles.add(new Tax(19, "Einkommenssteuer", 100));
        tiles.add(new Free(20, "Frei Parken"));
        tiles.add(new Street(21, "Praterstraße", 320, 80, 160));
        tiles.add(new Event(22, "Ereignisfeld"));
        tiles.add(new Street(23, "Gürtel", 340, 85, 170));
        tiles.add(new Station(24, "Bahnhof Mitte"));
        tiles.add(new Street(25, "Ringstraße", 360, 90, 180));
        tiles.add(new Event(26, "Ereignisfeld"));
        tiles.add(new Street(27, "Donauuferstraße", 380, 95, 190));
        tiles.add(new Tax(28, "Luxussteuer", 300));
        tiles.add(new Street(29, "Untere Donaustraße", 400, 100, 200));
        tiles.add(new GoToJail(30, "Gehe ins Gefängnis"));
        tiles.add(new Street(31, "Lassallestraße", 420, 105, 210));
        tiles.add(new Event(32, "Ereignisfeld"));
        tiles.add(new Street(33, "Währinger Straße", 440, 110, 220));
        tiles.add(new Station(34, "Bahnhof Flughafen"));
        tiles.add(new Street(35, "Billrothstraße", 460, 115, 230));
        tiles.add(new Event(36, "Ereignisfeld"));
        tiles.add(new Street(37, "Heiligenstädter Straße", 480, 120, 240));
        tiles.add(new Tax(38, "Strafsteuer", 150));
        tiles.add(new Street(39, "Stephansplatz", 500, 125, 250));
    }

    public Tile getTileAt(int position) {
        return tiles.get(position % tiles.size());
    }
}

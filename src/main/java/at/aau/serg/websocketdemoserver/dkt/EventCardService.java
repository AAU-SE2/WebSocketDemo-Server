package at.aau.serg.websocketdemoserver.dkt;

import java.util.List;
import java.util.Random;

public class EventCardService {
    private final List<String> cards = List.of(
            "Gehe 3 Felder zurück",
            "Gehe auf Start",
            "Zahle 200€ Strafe",
            "Erhalte 150€ Bonus",
            "Tausche Position mit einem Spieler",
            "Zahle Miete an jeden anderen Spieler"
    );
    private final Random rand = new Random();

    public String drawCard() {
        return cards.get(rand.nextInt(cards.size()));
    }
}

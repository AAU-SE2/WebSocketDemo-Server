package at.aau.serg.websocketdemoserver.dkt;

import at.aau.serg.websocketdemoserver.dkt.tiles.EventCardBank;
import at.aau.serg.websocketdemoserver.dkt.tiles.EventCardRisiko;

import java.util.List;
import java.util.Random;

public class EventCardService {

    private final List<EventCardRisiko> eventCardsRisiko = List.of(
            new EventCardRisiko("Gehe 3 Felder zurück", -3),
            new EventCardRisiko("Gehe 2 Felder vor", 2),
            new EventCardRisiko("Gehe 4 Felder zurück", -4),
            new EventCardRisiko("Gehe 4 Felder vor", 4)
    );

    private final List<EventCardBank> eventCardsBank = List.of(
            new EventCardBank("Für Unfallversicherung bezahlst du 200,-", -200),
            new EventCardBank("Für eine Autoreparatur bezahlst du 140,-", -140),
            new EventCardBank("Für die Auswertung einer Erfindung erhälst du 140,- aus öffentlichen Mitteln", 140),
            new EventCardBank("Die Bank zahlt dir an Dividenden 60,-", 60)
    );
    private final Random rand = new Random();

    public EventCardBank drawBankCard() {
        return eventCardsBank.get(rand.nextInt(eventCardsBank.size()));
    }

    public EventCardRisiko drawRisikoCard(){
        return eventCardsRisiko.get(rand.nextInt((eventCardsRisiko.size())));
    }
}

package at.aau.serg.websocketdemoserver.dkt;


import at.aau.serg.websocketdemoserver.messaging.dtos.EventCardBank;
import at.aau.serg.websocketdemoserver.messaging.dtos.EventCardRisiko;

import java.util.*;

public class GameHandler {
    private final Queue<EventCardRisiko> eventCardsRisiko;
    private final Queue<EventCardBank> eventCardsBank;

    public GameHandler(){
        List<EventCardRisiko> shuffledRisikoCards = new ArrayList<>(List.of(
                new EventCardRisiko( "Für Unfallversicherung bezahlst du 200,-", -200),
                new EventCardRisiko("Gehe um 4 Felder zurück", -4),
                new EventCardRisiko("Die Bank zahlt die an Dividenden 60,-", 60),
                new EventCardRisiko("Für unerlabutes Parken bezahlst du 10,-", -10),
                new EventCardRisiko("Für die Auswertung einer Erfindung erhälst du 140,- aus öffentlichen Mitteln", 140),
                new EventCardRisiko("Für eine Autoreparatur bezahlst du 140,-", -140),
                new EventCardRisiko("Gehe in den Arrest!", 0),
                new EventCardRisiko("Zahle 5,- Polizeistrafe", -5)
        ));

        List<EventCardBank> shuffledBankCards = new ArrayList<>(List.of(
                new EventCardBank("Für Gehsteigreinigung bezahle 40,-", -40),
                new EventCardBank("Die Bank bezahlt dir an Zinsen 25,-", 25),
                new EventCardBank("Bezahle deine Lebensversicherungsprämie 120,-", -120),
                new EventCardBank("Eine Geschäftsanbahnung bringt dir 35,- an Provision", 35),
                new EventCardBank("Für eine gewonnene Wette erhältst du 120,-", 120),
                new EventCardBank("Für Bankzinsen erhältst du 180,-", 180),
                new EventCardBank("An Versicherungskosten für deine Häuser bezahlst du 50,-", -50),
                new EventCardBank("Wegen Übertretung der Fahrordnung bezahle 15,-", -15),
                new EventCardBank("In der Klassenlotterie hast du 20,- gewonnen", 20)
        ));

        Collections.shuffle(shuffledRisikoCards);
        Collections.shuffle(shuffledBankCards);

        eventCardsRisiko = new LinkedList<>(shuffledRisikoCards);
        eventCardsBank = new LinkedList<>(shuffledBankCards);
    }

    public GameMessage handle(GameMessage msg) {
        switch (msg.getType()) {
            case "roll_dice":
                return handleRollDice(msg.getPayload());
            case"draw_event_card":
                return handleEventCard(msg.getPayload());
            default:
                return new GameMessage("error", "Unbekannter Typ: " + msg.getType());
        }
    }

    private GameMessage handleRollDice(String payload) {
        int dice = new java.util.Random().nextInt(6) + 1;
        return new GameMessage("dice_result", String.valueOf(dice));
    }

    private GameMessage handleEventCard(String playload){
        switch(playload){
            case "Risiko":
                EventCardRisiko risikoCard = eventCardsRisiko.poll();
                eventCardsRisiko.offer(risikoCard);
                return new GameMessage("risiko_card", risikoCard.getTitle() + ": " + risikoCard.getDescription());
            case "Bank":
                EventCardBank bankCard = eventCardsBank.poll();
                eventCardsBank.offer(bankCard);
                return new GameMessage("event_card", bankCard.getTitle() + ": " + bankCard.getDescription());
            default: return new GameMessage("error", "Unbekannter Typ:" + playload);
        }
    }


}

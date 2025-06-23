package at.aau.serg.websocketdemoserver.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BankCardDeck {

    private static final BankCardDeck instance = new BankCardDeck();
    private final List<BankCard> cards;

    private BankCardDeck() {
        cards = new ArrayList<>();
        cards.add(new BankCard(1, "Parkstrafe", "Du musst eine Parkstrafe zahlen.", -50));
        cards.add(new BankCard(2, "Bankirrtum", "Ein Bankirrtum zu deinen Gunsten.", 200));
        cards.add(new BankCard(3, "Versicherungsprämie", "Du erhältst eine Versicherungsprämie.", 100));
        cards.add(new BankCard(4, "Autoreparatur", "Du musst eine Autoreparatur bezahlen.", -150));
        cards.add(new BankCard(5, "Erbschaft", "Du erhältst eine Erbschaft.", 300));
        cards.add(new BankCard(6, "Spendenzahlung", "Du spendest für einen guten Zweck.", -100));
    }

    public static BankCardDeck get() {
        return instance;
    }

    public BankCard drawCard() {
        Collections.shuffle(cards);
        return cards.get(0);
    }
}

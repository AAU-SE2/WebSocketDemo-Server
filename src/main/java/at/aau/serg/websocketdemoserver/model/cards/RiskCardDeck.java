package at.aau.serg.websocketdemoserver.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RiskCardDeck {

    private static final RiskCardDeck instance = new RiskCardDeck();
    private final List<RiskCard> cards;

    private RiskCardDeck() {
        cards = new ArrayList<>();

        cards.add(new GoToJailRiskCard(1, "Jail Time", "Gehe direkt ins Gefängnis!"));
        cards.add(new EscapeRiskCard(2, "Freedom Pass", "Du bist frei und kannst das Gefängnis verlassen."));
        cards.add(new CashRiskCard(3, "Lottogewinn", "Du gewinnst im Lotto!", 150));
        cards.add(new CashRiskCard(4, "Steuernachzahlung", "Du musst Steuern zahlen!", -100));
        cards.add(new CashRiskCard(5, "Bonus", "Du erhältst einen Bonus von deinem Arbeitgeber.", 80));
    }

    public static RiskCardDeck get() {
        return instance;
    }

    public RiskCard drawCard() {
        Collections.shuffle(cards);
        return cards.get(0);
    }
}

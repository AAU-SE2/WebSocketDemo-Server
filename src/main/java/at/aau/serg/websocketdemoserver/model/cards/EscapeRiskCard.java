package at.aau.serg.websocketdemoserver.model.cards;

public class EscapeRiskCard extends RiskCard {

    public EscapeRiskCard(int id, String title, String description) {
        super(id, title, description);
    }

    @Override
    public RiskCardEffect getEffect() {
        return RiskCardEffect.ESCAPE_CARD;
    }
}

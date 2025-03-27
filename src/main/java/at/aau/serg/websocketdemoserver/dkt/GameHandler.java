package at.aau.serg.websocketdemoserver.dkt;

import java.util.Random;

public class GameHandler {
    public GameMessage handle(GameMessage msg) {
        switch (msg.getType()) {
            case "roll_dice":
                return handleRollDice(msg.getPayload());
            default:
                return new GameMessage("error", "Unbekannter Nachrichtentyp: " + msg.getType());
        }
    }

    private GameMessage handleRollDice(String payload) {
        int diceValue = new Random().nextInt(6) + 1; // Wert zwischen 1–6
        System.out.println("Server würfelt: " + diceValue);
        return new GameMessage("dice_result", String.valueOf(diceValue));
    }
}

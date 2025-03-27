package at.aau.serg.websocketdemoserver.dkt;


import java.util.Random;

public class GameHandler {

    public GameMessage handle(GameMessage msg) {
        switch (msg.getType()) {
            case "roll_dice":
                return handleRollDice(msg.getPayload());
            default:
                return new GameMessage("error", "Unbekannter Typ: " + msg.getType());
        }
    }

    private GameMessage handleRollDice(String payload) {
        int dice = new java.util.Random().nextInt(6) + 1;
        return new GameMessage("dice_result", String.valueOf(dice));
    }
}

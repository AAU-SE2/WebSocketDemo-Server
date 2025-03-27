package at.aau.serg.websocketdemoserver.dkt;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class GameWebSocketController {
    private final GameHandler gameHandler = new GameHandler();


    @MessageMapping("/dkt")
    @SendTo("/topic/dkt")
    public GameMessage handleGameMessage(@Payload GameMessage message) {
        System.out.println("DKT empfangen: " + message.getType());
        return gameHandler.handle(message);
    }
}

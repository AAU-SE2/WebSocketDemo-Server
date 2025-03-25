package at.aau.serg.websocketdemoserver;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GameWebSocketController {
    @MessageMapping("/dkt")
    @SendTo("/topic/dkt")
    public GameMessage handleGameMessage(@Payload GameMessage message) {
        System.out.println("DKT: " + message.getType() + " – " + message.getPayload());
        return message;
    }
}

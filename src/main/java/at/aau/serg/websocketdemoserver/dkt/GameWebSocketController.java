package at.aau.serg.websocketdemoserver.dkt;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.List;


@Controller
public class GameWebSocketController {
    private final GameHandler gameHandler = new GameHandler();

    private final SimpMessagingTemplate messagingTemplate;

    public GameWebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }


    @MessageMapping("/dkt")
    @SendTo("/topic/dkt")
    public GameMessage handleGameMessage(@Payload GameMessage message) {
        System.out.println("DKT empfangen: " + message.getType());

        GameMessage result = gameHandler.handle(message);

        // Nebenwirkung: weitere Nachrichten aus Queue senden
        List<GameMessage> extras = gameHandler.getExtraMessages();
        for (GameMessage extra : extras) {
            System.out.println("→ Extra: " + extra.getType());
            messagingTemplate.convertAndSend("/topic/dkt", extra);
        }

        return result; // erste Nachricht geht direkt an Sender
    }

}

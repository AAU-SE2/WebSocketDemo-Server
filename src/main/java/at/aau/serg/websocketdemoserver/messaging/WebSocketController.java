package at.aau.serg.websocketdemoserver.messaging;

import at.aau.serg.websocketdemoserver.messaging.dtos.OutputMessage;
import at.aau.serg.websocketdemoserver.messaging.dtos.StompMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class WebSocketController {



    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(StompMessage message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(),  time);
    }

}

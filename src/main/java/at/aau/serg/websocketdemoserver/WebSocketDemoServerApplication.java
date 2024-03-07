package at.aau.serg.websocketdemoserver;

import at.aau.serg.websocketdemoserver.messaging.dtos.OutputMessage;
import at.aau.serg.websocketdemoserver.messaging.dtos.StompMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class WebSocketDemoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketDemoServerApplication.class, args);
    }



}

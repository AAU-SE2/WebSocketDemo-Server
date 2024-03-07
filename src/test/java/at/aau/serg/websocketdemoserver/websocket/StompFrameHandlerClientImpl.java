package at.aau.serg.websocketdemoserver.websocket;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;
import java.util.concurrent.BlockingQueue;

public class StompFrameHandlerClientImpl implements StompFrameHandler {
    private BlockingQueue<String> messagesQueue;

    public StompFrameHandlerClientImpl(BlockingQueue<String> receivedMessagesQueue) {
        messagesQueue = receivedMessagesQueue;
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return String.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        // add the new message to the queue of received messages
        messagesQueue.add((String) payload);
    }

}

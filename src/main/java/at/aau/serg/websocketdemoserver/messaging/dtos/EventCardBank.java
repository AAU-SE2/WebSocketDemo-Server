package at.aau.serg.websocketdemoserver.messaging.dtos;

public class EventCardBank extends EventCard{

    public EventCardBank(String description, int action) {
        super(description, action);
        setTitle("BANK");
    }
}

package at.aau.serg.websocketdemoserver.messaging.dtos;

public class EventCardRisiko extends EventCard {

    public EventCardRisiko(String description, int action) {
        super(description, action);
        setTitle("RISIKO");
    }
}

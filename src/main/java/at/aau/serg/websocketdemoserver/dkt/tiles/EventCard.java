package at.aau.serg.websocketdemoserver.messaging.dtos;

import lombok.Getter;

public abstract class EventCard {
    @Getter
    private String title;
    @Getter
    private String description;
    @Getter
    private int amount;

    public EventCard(String description, int amount){
        this.description = description;
        this.amount = amount;
    }
    public EventCard(String title, String description, int amount){
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

}

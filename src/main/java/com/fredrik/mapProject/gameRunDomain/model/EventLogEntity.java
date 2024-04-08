package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import jakarta.persistence.*;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @EmbeddedId
    private EventId eventId;

    @Enumerated(EnumType.STRING)
    private Player playerNr;

    private int turn;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private String eventData;

    public EventId getEventId() {
        return eventId;
    }

    public void setEventId(EventId eventId) {
        this.eventId = eventId;
    }

    public Player getPlayerNr() {
        return playerNr;
    }

    public void setPlayerNr(Player playerNr) {
        this.playerNr = playerNr;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public Event getEvent() {

        switch (this.eventType) {
            case EXPLORE_EVENT -> {
                return new ExploreEvent(this.eventId, this.playerNr, this.turn, this.eventType, this.eventData);
            }
            case BUILD_EVENT -> {
                return new BuildEvent(this.eventId, this.playerNr, this.turn, this.eventType, this.eventData);
            }
            case CLAIM_TILE_EVENT -> {
                return new ClaimTileEvent(this.eventId, this.playerNr, this.turn, this.eventType, this.eventData);
            }
            default -> {
                return null;
            }
        }
    }

    public void setEvent(Event event) {
        this.eventData = event.parseToEventData();
        this.eventId = event.getEventId();
        this.eventType = event.getEventType();
        this.turn = event.getTurn();
        this.playerNr = event.getPlayerNr();
    }
}

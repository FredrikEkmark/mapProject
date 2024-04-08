package com.fredrik.mapProject.gameRunDomain.model;

import com.fredrik.mapProject.gamePlayDomain.Player;
import com.fredrik.mapProject.gamePlayDomain.model.MapCoordinates;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "event_log")
public class EventLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID eventId;

    @Column(nullable = false)
    private UUID gameId;

    @Enumerated(EnumType.STRING)
    private Player playerNr;

    private int turn;

    @Embedded
    private MapCoordinates primaryTileCoordinates;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private String eventData;

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getGameId() {
        return gameId;
    }

    public void setGameId(UUID gameId) {
        this.gameId = gameId;
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

    public MapCoordinates getPrimaryTileCoordinates() {
        return primaryTileCoordinates;
    }

    public void setPrimaryTileCoordinates(MapCoordinates primaryTileCoordinates) {
        this.primaryTileCoordinates = primaryTileCoordinates;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public Event getEvent() {

        switch (this.eventType) {
            case EXPLORE_EVENT -> {
                return new ExploreEvent(this.eventId, this.playerNr, this.turn, this.primaryTileCoordinates, this.eventType, this.eventData);
            }
            case BUILD_EVENT -> {
                return new BuildEvent(this.eventId, this.playerNr, this.turn, this.primaryTileCoordinates, this.eventType, this.eventData);
            }
            case CLAIM_TILE_EVENT -> {
                return new ClaimTileEvent(this.eventId, this.playerNr, this.turn, this.primaryTileCoordinates, this.eventType, this.eventData);
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
        this.primaryTileCoordinates = event.getPrimaryTileCoordinates();
        this.playerNr = event.getPlayerNr();
    }
}
